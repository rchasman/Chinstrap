(ns chinstrap.views.pages
  (:require [chinstrap.views.common :as template]
            [noir.response :as nr]
            [chinstrap.queries :as cq]
            [monger.collection :as mc])
  (:use [noir.core]
        [chinstrap.db]
        [clojure.data.json :only (json-str)]
        [hiccup.element]))

(defpage "/" []
  (render "/jobs"))

;Page listing the count of different states of Discovery Environment Jobs.
(defpage "/jobs" []
  (template/jobs-page
    (image "/img/logo.png")
    (javascript-tag "window.setInterval(getJobs,36000);")
    [:h3 "Discovery Environment Jobs Status"]
    [:br]
    [:div#inner
      [:h3.text "Running Apps: " [:span#running]]
      [:h3.text "Submitted Apps: " [:span#submitted]]
      [:h3.text "Completed Apps: " [:span#completed]]]
    [:br]
    [:div.collapsibleContainer {:title "Running App Details:"}
      [:div {}{:names (:name (:state (second (mc/find-maps "jobs" {:state.status "Running"}))))}]]
    [:div.collapsibleContainer {:title "Submitted App Details:"}
      [:div {}{:names (:name (:state (second (mc/find-maps "jobs" {:state.status "Submitted"}))))}]]
    [:br]
    (link-to "/components" "Discovery Environment Component Info")
    [:br]
    (link-to "/leaderboards" "Discovery Environment App Leaderboards")
    ))

;AJAX call from the Javascript file 'get-de-jobs.js'.
(defpage "/get-jobs" []
  (nr/json {:running (mc/count "jobs" {:state.status "Running"}),
            :submitted (mc/count "jobs" {:state.status "Submitted"}),
            :completed (mc/count "jobs" {:state.status "Completed"})}))

;Page listing count and info of Components with no transformation activities.
(defpage "/components" []
  (def i 0)
  (template/components-page
    (image "/img/logo.png")
    (javascript-tag "window.setInterval(getComponents,36000);")
    [:h3 "Discovery Environment Components Info"]
    [:br]
    [:div#inner
      [:h3.text "With Associated Apps: " [:span#with]]
      [:h3.text "Without Associated Apps: " [:span#without]]
      [:h3.text "Total Components: " [:span#all]]]
      [:br]
      [:div.collapsibleContainer {:title "Unused Componenent Details"}
        [:table
          [:tr [:th "#"]
              [:th "Name"]
              [:th "Version"]]
          (for [list (cq/unused-list)]
              [:tr
                [:td (var-get (def i (inc i)))]
                [:td (:name list)]
                [:td (or (:version list) "No Version")]])]]
      [:br]
      (link-to "/jobs" "Discovery Environment Jobs Status")
      [:br]
      (link-to "/leaderboards" " Discovery Environment App Leaderboards")
      [:br]
      [:br]))

;AJAX call from the Javascript file 'get-components.js'.
(defpage "/get-components" []
  (nr/json {:all (cq/all-count)
            :without (cq/without-count)
            :with (cq/with-count)
           }))

(defpage "/leaderboards" []
  (def i 0)
  (template/leaderboard-page
    (image "/img/logo.png")
    [:br]
    [:div.collapsibleContainer {:title "Discovery Enviroment App Leaderboard"}
      [:table
        [:tr [:th "#"]
             [:th "Username"]
             [:th "Count"]]
        (for [list (cq/leader-list)]
             [:tr
               [:td (var-get (def i (inc i)))]
               [:td (:username list)]
               [:td (or (:count list) "None Yet")]])]]
    [:br]
    (link-to "/jobs" "Discovery Environment Jobs Status")
    [:br]
    (link-to "/components" "Discovery Environment Component Info")))
