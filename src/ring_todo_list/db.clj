(ns ring-todo-list.db
  (:require [monger.core :as mg]
            [monger.credentials :as mcr]
            [monger.collection :as mc])
  (:import org.bson.types.ObjectId))

; TODO it is recommended to use ObjectId so that your documents are immutable

(defn db-connection! []
  (let [cred (mcr/create "todo-lists" "todo-lists" "example")
        conn (mg/connect-with-credentials "localhost" 27017 cred)]
    conn))

(defn insert-todo-list! [conn todo-list]
  (let [db (mg/get-db conn "todo-lists")]
    (mc/insert-and-return db "collection" todo-list)
    ))

(defn get-all []
  (let [conn (db-connection!)]
    (mc/find-maps (mg/get-db conn "todo-lists") "collection")))

(defn get-one [^String object-id]
  (let [conn (db-connection!)]
    (mc/find-one-as-map
      (mg/get-db conn "todo-lists") "collection"
      {:_id (ObjectId. object-id)})))

(defn close! [conn]
  (mg/disconnect conn))

(comment
  (let [conn (db-connection!)]
    (insert-todo-list! conn)
    (close! conn))
  (let [conn (db-connection!)]
    (mc/find-maps (mg/get-db conn "todo-lists") "collection"))
  (let [conn (db-connection!)]
    (mc/find-one-as-map (mg/get-db conn "todo-lists") "collection" {:_id (ObjectId. "60ab92b90b6615f55ab0b21c")}))
  )