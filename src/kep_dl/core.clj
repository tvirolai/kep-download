(ns kep-dl.core
  (:require [clj-http.client :as client]
            [net.cgrand.enlive-html :as html]
            [clojure.java.io :as io]
            [clojure.string :as s])
  (:gen-class))

(defonce dom
  (-> "http://hipster.fi/kep/shows" client/get :body html/html-snippet))

(def shows
  (html/select dom [:body :table :tbody :tr]))

(defrecord Show [url description])

(defn show-details [show]
  "Returns a record containing a show url and title"
  (let [details (-> show :content second :content first)]
    (Show. (get-in details [:attrs :href])
           (first (:content details)))))

(defn fetch-file! [url]
  (let [req (client/get url {:as :byte-array :throw-exceptions false})]
    (if (= (:status req) 200)
      (:body req))))

(defn save-file! [show]
  (let [filename (str "data/" (last (s/split (:url show) #"/")))]
    (if-not (.exists (io/as-file filename))
      (let [p (fetch-file! (:url show))]
        (if-not (nil? p)
          (with-open [w (io/output-stream filename)]
            (.write w p)))))))

(defn download-all! []
  (loop [episodes (map show-details shows)]
    (if (empty? episodes)
      (println "Done!")
      (do
        (println (str "Downloading episode: " (:description (first episodes))))
        (save-file! (first episodes))
        (recur (rest episodes))))))

(defn -main [& args]
  (download-all!))
