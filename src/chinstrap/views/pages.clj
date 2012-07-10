(ns chinstrap.views.pages
  (:require [chinstrap.views.common :as template]
            [chinstrap.models.ajax :as ajax]
            [noir.response :as nr]
            [chinstrap.queries :as cq]
            [monger.collection :as mc])
  (:use [noir.core]
        [chinstrap.db]
        [hiccup.element]))

(defn apps-that-are
  "This function returns the application names currently operating at the passed state.
  E.G. (apps-that-are \"Completed\")"
  [state]
    (map #(str (:name (:state %)) "<br>")
      (mc/find-maps "jobs" {:state.status (str state)} [:state.name])))

(defpage "/" []
  (render "/main"))

(defpage "/main" []
  (template/main-page
    (image "/img/logo.png")
    [:h3 "Discovery Environment Status"]
    [:br][:br]
    [:div#inner]
    [:br]
    [:input#date {:type "text" :onChange "getInfo()"}]
    [:br]
    [:br]
    (link-to "/components" "Discovery Environment Component Info")
    [:br]
    (link-to "/apps" "Discovery Environment App Info")
    [:br]))

;Page listing the count of different states of Discovery Environment Apps.
(defpage "/apps" []
  (template/apps-page
    (image "/img/logo.png")
    (javascript-tag "window.setInterval(getApps,36000);")
    [:h3 "Discovery Environment App Status"]
    [:br]
    [:div#inner
      [:h3.left "Running Apps:" [:span#running.right]]
      [:h3.left "Submitted Apps:" [:span#submitted.right]]
      [:h3.left "Completed Apps:" [:span#completed.right]]]
    [:br]
    [:div.collapsibleContainer {:title "Currently Running Apps"}
      [:div (apps-that-are "Running")]]
    [:br]
    [:div.collapsibleContainer {:title "Submitted Apps"}
      [:div (apps-that-are "Submitted")]]
    [:br]
    (link-to "/main" "Discovery Environment Status")
    [:br]
    (link-to "/components" "Discovery Environment Component Info")
    [:br]))

;Page listing count and info of Components with no transformation activities.
(defpage "/components" []
  (def i 0) ;doing this wrong and I know it (for table increment)
  (def j 0) ;doing this wrong and I know it (for table increment)
  (template/components-page
    (image "/img/logo.png")
    (javascript-tag "window.setInterval(getComponents,36000);")
    [:h3 "Discovery Environment Components Info"]
    [:br]
    [:div#inner
      [:h3.left "With Associated Apps:" [:span#with.right]]
      [:h3.left "Without Associated Apps:" [:span#without.right]]
      [:h3.left "Total Components:" [:span#all.right]]]
      [:br]
      [:div.collapsibleContainer {:title "Unused Componenent Details"}
      [:br]
        [:table
          [:tr [:th ""]
               [:th "Name"]
               [:th "Version"]]
          (for [list (cq/unused-list)]
               [:tr
                 [:td.center (var-get (def i (inc i)))]
                 [:td (:name list)]
                 [:td.center (or (:version list) "No Version")]])]]
      [:br]
      [:div.collapsibleContainer {:title "Discovery Enviroment App Leaderboard"}
        [:br]
        [:table
          [:tr [:th ""]
               [:th "Name"]
               [:th "Count"]]
          (for [list (cq/leader-list)]
               [:tr
                 [:td.center (var-get (def j (inc j)))]
                 [:td (:name list)]
                 [:td.center (:count list)]])]]
      [:br]
      (link-to "/main" "Discovery Environment Status")
      [:br]
      (link-to "/apps" "Discovery Environment App Status")
      [:br]))
