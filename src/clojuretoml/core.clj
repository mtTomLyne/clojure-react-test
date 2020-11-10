(ns clojuretoml.core
  (:require [org.httpkit.server :refer [run-server]]
            [clj-time.core :as t]
            [compojure.core :refer :all]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.middleware.defaults :refer :all]
            [clojure.pprint :as pp]
            [compojure.route :as route]
            [taoensso.faraday :as dynamo]
            [clojure.data.json :as json])
  (:import (com.amazonaws.services.dynamodbv2.model ConditionalCheckFailedException TransactionCanceledException))
)
(import java.util.Date)


(def client-opts
  {;;; For DynamoDB Local just use some random strings here, otherwise include your
   ;;; production IAM keys:
   :access-key "<AWS_DYNAMODB_ACCESS_KEY>"
   :secret-key "<AWS_DYNAMODB_SECRET_KEY>"
   :endpoint   "http://localhost:8000"                   ; For DynamoDB Local
   ;; :endpoint "http://dynamodb.eu-west-1.amazonaws.com" ; For EU West 1 AWS region

   ;;; You may optionally provide your own (pre-configured) instance of the Amazon
   ;;; DynamoDB client for Faraday functions to use.
   ;; :client (AmazonDynamoDBClientBuilder/defaultClient)
   })

(defn square [num] (* num num))

(defn squareEndpoint [req]
  (let [result (square (Float/parseFloat (:number (:params req))))]
  		(let [response {:status  200
    		:headers {"Content-Type" "text/html"}
    		:body    (->>
    		(str result))}]
    		(println "Incoming request: " req)
    		(println "Squaring number " (:number (:params req)))
    		(println "Returning number " (str result))
      (dynamo/put-item client-opts :Results
      		{:input (str (:number (:params req))) :output (str result)})
    (str "{\"input\":\"" (:number (:params req)) "\", \"output\":\"" result "\"}")
    )
  )
)



(defn getDoctorEndpoint [req]
   (let [response {:status  200
      :headers {"Content-Type" "text/html"}
      :body    (->>
      (str "Hello world"))}]
      (println "Incoming request: " req)
      (println "Incoming GET on /doctors at" (str (t/time-now)))
    (json/write-str (dynamo/scan client-opts :people))
  )
)

(defn deleteDoctorEndpoint [req]
	 (let [deleteKey (str (:id (:params req)))]
    (let [response {:status  204
      :headers {"Content-Type" "text/html"}
      :body    (->>
      (str "Hello world"))}]
      (println "Incoming request: " req)
      (println "Incoming DELETE on /doctors at" (str (t/time-now)))
      (println "Attempting to DELETE doctor id: " (str deleteKey))
		    (json/write-str (dynamo/delete-item client-opts :people {:id (str deleteKey)}))
    )
  )
)	


(defn postDoctorEndpoint [req]
	 (let [postKey (str (:id (:params req)))]
	   (let [postCategory (str (:category (:params req)))]
			   (let [postImage (str (:image (:params req)))]
					   (let [postDescription (str (:description (:params req)))]
							   (let [postSpecialityName (str (:specialityName (:params req)))]
							   		(let [postName (str (:name (:params req)))]
										    (let [response {:status  201
										      :headers {"Content-Type" "text/html"}
										      :body    (->>
										      (str "Hello world"))}]
										      (println "Incoming request: " req)
										      (println "Incoming POST on /doctors at" (str (t/time-now)))
												    (json/write-str (dynamo/put-item client-opts :people 
												    		{:id (str postKey) 
												    			:category (str postCategory)
												    			:image (str postImage)
												    			:description (str postDescription)
												    			:specialityName (str postSpecialityName)
												    			:name (str postName)}))
										    )
							   		)
							   )
					   )
			   )
    )
  )
)	

; define routes.
(defroutes app-routes
		(GET "/square" [] squareEndpoint)
		(GET "/db" [] dbTest)
		(GET "/doctors" [] getDoctorEndpoint)
		(DELETE "/doctors" [] deleteDoctorEndpoint)
		(POST "/doctors" [] postDoctorEndpoint)
		(route/not-found "<h1>Page not found</h1>"))

(def app
  (-> app-routes
    (wrap-defaults (assoc-in site-defaults [:security :anti-forgery] false))
    (wrap-cors
     :access-control-allow-origin [#".*"]
     :access-control-allow-methods [:get :put :post :delete :options]
     )
  )
)

; starting point of server.
(defn -main [& args]
  (run-server #'app {:port 8080})
  (println "Server started on port 8080")
)