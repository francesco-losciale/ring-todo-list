(ns ring-todo-list.core
  (:require [reitit.ring :as ring]
            [ring.adapter.jetty :refer [run-jetty]]))

(def app
  (ring/ring-handler
    (ring/router
      ["/api/hello" {:get {
                        :handler
                        (fn [request]
                          {:status 200
                           :headers {"Content-Type" "text/plain"}
                           :body "Hello World"})
                        }}])))

(defn -main [& args]
  (run-jetty app {:port 3000}))