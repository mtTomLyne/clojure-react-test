(defproject clojuretoml "1.0.0"
  :description "toms test clojure project"
  :min-lein-version "2.7.1"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [http-kit "2.2.0"]
                 [clj-time "0.14.0"]
                 [com.cemerick/friend "0.1.5" :exclusions [ring/ring-core]]
                 [ring-server "0.2.7"]
                 [ring/ring-json "0.5.0"]
                 [ring-middleware-format "0.7.4"]
                 [ring-cors "0.1.13"]
                 [http-kit "2.3.0"]
                 [ring/ring-defaults "0.3.2"]
                 [compojure "1.6.0"]]
  :main clojuretoml.core)
