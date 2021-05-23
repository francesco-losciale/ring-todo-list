(ns ring-todo-list.db
  (:require [monger.core :as mg]))

; TODO
; create apis to read and save lists

(defn connect []
  (let [conn (mg/connect {:host "localhost" :port 27017})
        db   (mg/get-db conn "todo-lists")]
    conn))