(ns clojuretoml.core
  (:require [org.httpkit.server :refer [run-server]]
            [clj-time.core :as t]
            [compojure.core :refer :all]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.middleware.defaults :refer :all]
            [clojure.pprint :as pp]
            [compojure.route :as route]
            [taoensso.faraday :as dynamo])
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
                         {:input (str (:number (:params req))) :output (str result)
                          })
      ;; there's probably a much, MUCH nicer way to do this... would advise wearing protective goggles when reading the next line of code
    (str "{\"input\":\"" (:number (:params req)) "\", \"output\":\"" result "\"}")))
   )

(defn dbTest [req]
						(let [result 5]
      (let [response {:status  200
      :headers {"Content-Type" "text/html"}
      :body    (->>
      (str result))}]
      (dynamo/put-item client-opts :Results
                         [:Results/input (:number (:params req)) :Results/output (str result)]  ; Primary key named "id", (:n => number type)
                         {:throughput {:read 1 :write 1} ; Read & write capacity (units/sec)
                          :block? true ; Block thread during table creation
                          })
      ;; there's probably a much, MUCH nicer way to do this... would advise wearing protective goggles when reading the next line of code
    (str "{\"input\":\"" result "\", \"output\":\"" result "\"}"))
				
)
						)
      

; define routes.
(defroutes app-routes
           (GET "/square" [] squareEndpoint)
           (GET "/db" [] dbTest)
           (route/not-found "<h1>Page not found</h1>"))

(def app
  (-> app-routes
      (wrap-defaults site-defaults)
      (wrap-cors
     :access-control-allow-origin [#".*"]
     :access-control-allow-methods [:get :put :post :delete :options])
      ))

; starting point of server.
(defn -main [& args]
  (run-server #'app {:port 8080})
  (println "Server started on port 8080"))