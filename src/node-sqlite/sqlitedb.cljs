(ns find.app.sqlitedb
  (:require
   [clojure.core.async :as a :refer [chan go go-loop <! >!  take! put! offer! poll! alt! alts! close!
                                     pub sub unsub mult tap untap mix admix unmix pipe
                                     timeout to-chan  sliding-buffer dropping-buffer
                                     pipeline pipeline-async]]
   [clojure.core.async.impl.protocols :refer [closed?]]
   [clojure.string]
   [clojure.pprint :refer [pprint]]
   [cljs.core.async.interop :refer-macros [<p!]]
   [goog.string.format :as format]
   [goog.string :refer [format]]
   [goog.object]
   [cljs.reader :refer [read-string]]))

(defonce fs (js/require "fs-extra"))
(defonce path (js/require "path"))
(defonce crypto (js/require "crypto"))
(defonce Sqlite3 (js/require "sqlite3"))

(defonce ^:private registryA (atom {}))

#_(defn start
    [{:keys [:peer-index
             :data-dir
             :id]
      :or {id :main}
      :as opts}]
    (go
      (let [stateA (atom nil)
            db-filename (.join path data-dir "db.sqlite3")
            db (Sqlite3.Database. db-filename)
            transact| (chan (sliding-buffer 100))
            torrent| (chan (sliding-buffer 100))]

        (.serialize db
                    (fn []
                      (.run db "CREATE TABLE IF NOT EXISTS torrents (infohash TEXT PRIMARY KEY)")
                      #_(let [statement (.prepare db "INSERT INTO lorem VALUES (?)")]
                          (doseq [i (range 0 10)]
                            (.run statement (str "Ipsum " i)))
                          (.finalize statement))

                      (.get db "SELECT COUNT(rowid) FROM torrents"
                            (fn [error row]
                              (pprint (js->clj row))))
                      #_(.each db "SELECT rowid AS id, info FROM lorem"
                               (fn [error row]
                                 (println (str (. row -id) ":" (. row -info)))))))

        #_(go
            (loop []
              (<! (timeout 10000))
              (.get db "SELECT COUNT(rowid) FROM torrents"
                    (fn [error row]
                      (pprint (js->clj row))))
              (recur)))

        (go
          (loop [batch (transient [])]
            (when-let [value (<! torrent|)]

              (cond

                (= (count batch) 10)
                (do
                  (put! transact| (persistent! batch))
                  (recur (transient [])))

                :else
                (recur (conj! batch value))))))

        (go
          (loop []
            (when-let [batch (<! transact|)]
              (.serialize db
                          (fn []
                            (.run db "BEGIN TRANSACTION")
                            (let [statement (.prepare db "INSERT INTO torrents(infohash) VALUES (?) ON CONFLICT DO NOTHING")]
                              (doseq [{:keys [infohash]} batch]
                                (.run statement infohash))
                              (.finalize statement))
                            (.run db "COMMIT")))
              (recur))))
        (reset! stateA {:db db
                        :torrent| torrent|})
        (swap! registryA assoc id stateA)
        stateA)))

(defn stop
  [{:keys [:id]
    :or {id :main}}]
  (let [result| (chan 1)]
    (if-let [stateA (get @registryA id)]
      (.close (:db @stateA)
              (fn [error]
                (if error
                  (println ::db-close-error error)
                  (println ::db-closed-ok))
                (swap! registryA dissoc id)
                (close! result|)))
      (close! result|))
    result|))


(defn start
  [{:keys [:peer-index
           :data-dir
           :id]
    :or {id :main}
    :as opts}]
  (go
    (let [db-filename (.join path data-dir "db.sqlite3")
          db (Sqlite3.Database. db-filename)]

      (.serialize db)

      (.run db "CREATE TABLE IF NOT EXISTS foo 
                              (infohash TEXT PRIMARY KEY,
                               name TEXT)")

      #_(doseq [i (range 0 1000)]
          (let [done| (chan 1)]
            (println i)
            (.run db "BEGIN TRANSACTION")
            (let [statement (.prepare db "INSERT INTO foo(infohash,name) VALUES (?,?) ON CONFLICT DO NOTHING")]
              (doseq [j (range 0 1000)]
                (.run statement
                      (.toString (.randomBytes crypto 20) "hex")
                      (str "some long name foo bar " i j)))
              (.finalize statement))
            (.run db "COMMIT" (fn []
                                (close! done|)))

            (<! done|)))

      (.parallelize db)

      (.get db "SELECT COUNT(rowid) FROM foo"
            (fn [error row]
              (pprint (js->clj row))))

      (.all db "SELECT infohash,name FROM foo LIMIT 10"
            (fn [error rows]
              (pprint (js->clj rows)))))))