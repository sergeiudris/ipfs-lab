(ns notepad.dockerode1
  (:require
   [clojure.core.async :as a :refer [chan go go-loop <! >!  take! put! offer! poll! alt! alts! close!
                                     pub sub unsub mult tap untap mix admix unmix pipe
                                     timeout to-chan  sliding-buffer dropping-buffer
                                     pipeline pipeline-async]]
   [clojure.core.async.impl.protocols :refer [closed?]]
   [clojure.string :as str]
   [cljs.core.async.interop :refer-macros [<p!]]
   [goog.string.format :as format]
   [goog.string :refer [format]]
   [cljs.reader :refer [read-string]]))

(defonce fs (js/require "fs"))
(defonce path (js/require "path"))
(defonce Docker (js/require "dockerode"))

(comment

  (def docker (Docker. (clj->js {"socketPath" "/var/run/docker.sock"})))

  (go
    (let [images (<p! (.listImages docker))]
      (println (count images))))

  ;; -it -p 5080:5080 --network dgraph_default -p 6080:6080 -v ~/zero:/dgraph dgraph/dgraph:v20.11.1 

  (go
    (let [volume-name "ipfs-cube-dgraph"
          result (<p! (.createVolume docker (clj->js {"Name" volume-name})))
          volumes (<p! (.listVolumes docker))
          volume (.getVolume docker volume-name)
          response-remove (<p! (.remove volume))]
      (println result)
      #_(println volumes)
      (println (.-name volume))
      (println (type response-remove))))

  (go
    (let [docker (Docker. (clj->js {"socketPath" "/var/run/docker.sock"}))
          volume-name "ipfs-cube-dgraph"
          volume (<p! (.createVolume docker (clj->js {"Name" volume-name})))]
      (<p! (.createVolume docker (clj->js {"Name" volume-name})))
      #_(.pull docker "dgraph/dgraph:v20.11.1"
               (fn [err stream]
                 (.pipe stream (.-stdout js/global.process))))
      (.run docker
            "dgraph/dgraph:v20.11.1"
            #js ["dgraph" "zero" "--my=zero:5080"]
            (.-stdout js/global.process)
            (clj->js {"Hostname" "ipfs-cube-zero"
                      "ExposedPorts" {"5080/tcp" {}}
                      "HostConfig"
                      {"Binds"
                       ["ipfs-cube-dgraph:/dgraph"]}
                      "NetworkingConfig"
                      {"EndpointsConfig"
                       {"ipfs-cube-network"
                        {"Aliases" ["zero"]}}}})
            (clj->js {}))
      (.run docker
            "dgraph/dgraph:v20.11.1"
            #js ["dgraph" "alpha" "--my=alpha:7080" "--zero=zero:5080"]
            (.-stdout js/global.process)
            (clj->js {"Hostname" "ipfs-cube-alpha"
                      "ExposedPorts" {"8080/tcp" {}
                                      "9080/tcp" {}}
                      "HostConfig"
                      {"Binds"
                       ["ipfs-cube-dgraph:/dgraph"]
                       "PortBindings"
                       {"8080/tcp"
                        [{"HostPort" "8080"}]}}
                      "NetworkingConfig"
                      {"EndpointsConfig"
                       {"ipfs-cube-network"
                        {"Aliases" ["alpha"]}}}})
            (clj->js {}))
      (.run docker
            "dgraph/dgraph:v20.11.1"
            #js ["dgraph-ratel"]
            (.-stdout js/global.process)
            (clj->js {"Hostname" "ipfs-cube-ratel"
                      "ExposedPorts" {"8000/tcp" {}}
                      "HostConfig"
                      {"Binds"
                       ["ipfs-cube-dgraph:/dgraph"]
                       "PortBindings"
                       {"8000/tcp"
                        [{"HostPort" "8000"}]}}
                      "NetworkingConfig"
                      {"EndpointsConfig"
                       {"ipfs-cube-network"
                        {"Aliases" ["ratel"]}}}})
            (clj->js {}))))




  ;;
  )