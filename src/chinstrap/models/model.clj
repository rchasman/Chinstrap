(ns chinstrap.models.model
  (:require [noir.response :as nr]
            [chinstrap.models.sqlqueries :as cq]
            [monger.collection :as mc]
            [clj-time.core :as time]
            [clj-time.coerce :as coerce]
            [clojure.walk :as walk]
            [clojure.tools.logging :as log])
  (:use [noir.core]))

(defn format-graph-data
  "This function takes in dates and their counts and parses them into a JSON
  object for easy graph data parsing in javascript."
  [data]
  (map
    #(hash-map
      :date (key %)
      :count (val %))
    data))

(defn get-app-names
  "This function returns the application names currently operating at the passed state.
  E.G. (get-apps-names \"Completed\")"
  [state]
    (apply str
      (map #(str (:name (:state %)) "<br>")
        (mc/find-maps "jobs" {:state.status (str state)} [:state.name]))))

;currently unused
(defpage "/get-all-apps" []
  (nr/json
    (rest
      (map #(str (:submission_date (:state %)))
        (mc/find-maps "jobs" {} [:state.submission_date])))))

;currently unused
(defpage "/get-failed-apps" []
  (nr/json
    (mc/find-maps "jobs" {:state.status {"$in" ["Failed"]}})))

;AJAX call from the Javascript file 'resources/public/js/day-graph.js' for graph data.
(defpage "/get-day-data" []
  (nr/json
    (format-graph-data
    (into (sorted-map) (reduce #(assoc %1 %2 (inc (%1 %2 0))) {}
      (map #(* 86400000 (long (/ (Long/parseLong (str %)) 86400000)))
        (rest (map #(:submission_date (:state %))
          (mc/find-maps "jobs" {:state.status {"$in" ["Completed"]}} [:state.submission_date])))))))))

;AJAX call from the Javascript file 'resources/public/js/month-graph.js' for graph data.
(defpage "/get-month-data" []
  (nr/json
    (format-graph-data
    (into (sorted-map) (reduce #(assoc %1 %2 (inc (%1 %2 0))) {}
      (map #(* 86400000 (long (/ (Long/parseLong (str %)) 86400000)))
        (rest (map #(:submission_date (:state %))
          (mc/find-maps "jobs" {:state.status {"$in" ["Completed"]}} [:state.submission_date])))))))))

;AJAX call from the Javascript file 'resources/public/js/get-info.js'.
(defpage "/get-info/:date" {:keys [date]}
  (nr/json {:tools
    (cq/count-apps
      (map #(str (:analysis_id (:state %)))
        (mc/find-maps "jobs" {:state.submission_date
          {"$gte" (read-string date) "$lt" (+ 86400000 (read-string date))}}
            [:state.analysis_id])))}))

;AJAX call from the Javascript file 'resources/public/js/get-apps.js'.
(defpage "/get-apps" []
  (nr/json {:running (mc/count "jobs" {:state.status "Running"}),
            :submitted (mc/count "jobs" {:state.status "Submitted"}),
            :failed (mc/count "jobs" {:state.status "Failed"}),
            :completed (mc/count "jobs" {:state.status "Completed"}),
            :running-names (str (get-app-names "Running")),
            :failed-names (str (get-app-names "Failed")),
            :submitted-names (str (get-app-names "Submitted"))}))

;AJAX call from the Javascript file 'resources/public/js/get-components.js'.
(defpage "/get-components" []
  (nr/json {:all (cq/all-app-count)
            :without (cq/unused-app-count)
            :with (cq/used-app-count)}))
