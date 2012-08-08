(ns chinstrap.views.common
  (:use [noir.core :only [defpartial]]
        [hiccup.page]
        [hiccup.element]))

(defpartial global [title]
  [:head
    [:title (str "Chinstrap - " title)]
    (include-css
      "/css/reset.css"
      "/css/style.css")
    (include-js
      "https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"
      "http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.9/jquery-ui.min.js"
      "/js/csv-parser.js")])

(defpartial graph-nav []
    [:div#graph-nav
      [:span.vbr]
      [:li#day.nav (link-to "/graph/day" "Day")]
      [:span.vbr]
      [:li#month.nav (link-to "/graph/month" "Month")]
      [:span.vbr]])

(defpartial navbar []
  [:div#navbar
    [:span.vbr]
    [:li.nav (link-to "/info" "Info")]
    [:div.vbr]
    [:li.nav (link-to "/apps" "Apps")]
    [:div.vbr]
    [:li.nav (link-to "/components" "Components")]
    [:div.vbr]
    [:li.nav (link-to "/graph" "Graphs")]
    [:div.vbr]])

(defpartial wrapper [& content]
  [:div#wrapper
    (image {:id "logo" :alt "iPlant Logo"} "/img/logo.png")
    [:br]
    content]
  [:br])

(defpartial footer []
  [:div#footer])

(defpartial page [& content]
  (navbar)
  (wrapper content)
  (footer))

(defpartial info-page [& content]
  (html5
    [:head
      (global "Info")
      (include-css "http://ajax.googleapis.com/ajax/libs/jqueryui/1.7.2/themes/cupertino/jquery-ui.css")
      (include-js "/js/get-info.js"
                  "/js/info-script.js"
                  "/js/lib/mousetrap.min.js")])
    [:body
      (page content)])

(defpartial apps-page [& content]
  (html5
    [:head
      (global "Apps")
      (include-js "/js/get-apps.js"
                  "/js/collapsible-panel.js")]
    [:body
      (javascript-tag "$(document).ready(function(){
        $('.collapsibleContainer').collapsiblePanel();});")
      (page content)]))

(defpartial components-page [& content]
  (html5
    [:head
      (global "Components")
      (include-js "/js/get-components.js"
                  "/js/collapsible-panel.js")]
    [:body
      (javascript-tag "$(document).ready(function(){
        $('.collapsibleContainer').collapsiblePanel();});")
      (page content)]))

(defpartial graph-page [content]
    [:h3 "DE Apps Completed Over Time"]
    [:br]
    (graph-nav)
    [:br]
    [:div#chart content [:div#loader]]
    [:h5.right "Data Starting from: " [:span#firstDate]])

(defpartial day-page [& content]
  (html5
    [:head
      (global "Graph - by Day")
      (include-js "/js/lib/spin.min.js"
                  "/js/lib/underscore-min.js"
                  "/js/spinner.js"
                  "/js/lib/amcharts.js"
                  "/js/day-graph.js")]
    [:body
      (page (graph-page content))]))

(defpartial month-page [& content]
  (html5
    [:head
      (global "Graph - by Month")
      (include-js "/js/lib/spin.min.js"
                  "/js/lib/underscore-min.js"
                  "/js/spinner.js"
                  "/js/lib/amcharts.js"
                  "/js/month-graph.js")]
    [:body
      (page (graph-page content))]))

(defpartial raw-page [& content]
  (html5
    [:head
      (global "Raw Data")]
    [:body
      (page content)]))
