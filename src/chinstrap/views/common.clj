(ns chinstrap.views.common
  (:use [noir.core :only [defpartial]]
        [hiccup.page]))

(defpartial Jobs-Page [& content]
    (html5
      [:head
        [:title "Discovery Enviroment Jobs Status"]
        (include-css "/css/reset.css"
                     "/css/style.css"
                     "http://fonts.googleapis.com/css?family=Arvo:400"
                     "http://fonts.googleapis.com/css?family=Ubuntu")
        (include-js  "/js/get-de-jobs.js")]
      [:body [:div#wrapper content]]))

(defpartial Components-Page [& content]
    (html5
      [:head
        [:title "Components Without Transformations"]
        (include-css "/css/reset.css"
                     "/css/style.css"
                     "http://fonts.googleapis.com/css?family=Arvo:400"
                     "http://fonts.googleapis.com/css?family=Ubuntu")
        (include-js  "/js/get-components.js")]
      [:body [:div#wrapper content]]))

(defpartial Leaderboard-Page [& content]
    (html5
      [:head
        [:title "Discovery Enviroment App Leaderboard"]
        (include-css "/css/reset.css"
                     "/css/style.css"
                     "http://fonts.googleapis.com/css?family=Arvo:400"
                     "http://fonts.googleapis.com/css?family=Ubuntu")]
      [:body [:div#wrapper content]]))
