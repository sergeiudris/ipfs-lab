(ns notepad.docker-from-browser1
  (:require
   [clojure.core.async :as a :refer [chan go go-loop <! >!  take! put! offer! poll! alt! alts! close!
                                     pub sub unsub mult tap untap mix admix unmix pipe
                                     timeout to-chan  sliding-buffer dropping-buffer
                                     pipeline pipeline-async]]
   [cljs.core.async.impl.protocols :refer [closed?]]
   [cljs.core.async.interop :refer-macros [<p!]]
   [goog.string.format :as format]
   [goog.string :refer [format]]
   [goog.object]
   [clojure.string :as str]
   [cljs.reader :refer [read-string]]))


(comment

  ;; dockerd started with:
  ;; /usr/bin/dockerd -H fd:// -H=tcp://0.0.0.0:2375 --api-cors-header="*" --containerd=/run/containerd/containerd.sock

  (println ::foo)

  (js/fetch "http://localhost:2375/images/json")

  (->
   (js/fetch "http://localhost:2375/info")
   (.then #(.json %))
   (.then js/console.log))

  (->
   (js/fetch "http://localhost:2375/version")
   (.then #(.json %))
   (.then js/console.log))

  (->
   (js/fetch "http://localhost:2375/containers/create"
             (clj->js {"method" "post"
                       "headers" {"Content-Type" "application/json"}
                       "body" (-> {"Cmd"
                                   ["date"]
                                   #_["tail -f /dev/null"]
                                   "AttachStdout" true
                                   "AttachStderr" true
                                   "Image" "ubuntu:20.04"}
                                  (clj->js)
                                  (js/JSON.stringify))}))
   (.then #(.json %))
   (.then (fn [response]
            (js/console.log response)
            (->
             (js/fetch (format "http://localhost:2375/containers/%s/start" (aget response "Id"))
                       (clj->js {"method" "post"
                                 "headers" {"Content-Type" "application/json"}
                                 "body" (-> {}
                                            (clj->js)
                                            (js/JSON.stringify))}))
             (.then #(.json %))
             (.then js/console.log)))))
  
  ;;
  )