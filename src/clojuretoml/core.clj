(ns clojuretoml.core
  (:require [org.httpkit.server :refer [run-server]]
            [clj-time.core :as t]
            [compojure.core :refer :all]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.middleware.defaults :refer :all]
            [clojure.pprint :as pp]
            [compojure.route :as route]))
(import java.util.Date)

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
      ;; there's probably a much, MUCH nicer way to do this... would advise wearing protective goggles when reading the next line of code
    (str "{\"input\":\"" (:number (:params req)) "\", \"output\":\"" result "\"}")))
   )

; define routes.
(defroutes app-routes
           (GET "/square" [] squareEndpoint)
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