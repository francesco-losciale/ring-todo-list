(ns ring-todo-list.db
  (:require [monger.core :as mg]))

(defn connect []
  (let [conn (mg/connect {:host "localhost" :port 27017})
        db   (mg/get-db conn "todo-lists")]
    conn))