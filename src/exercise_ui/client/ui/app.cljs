(ns exercise-ui.client.ui.app
  (:require
    [bloom.commons.pages :refer [path-for]]
    [bloom.commons.fontawesome :as fa]
    [re-frame.core :refer [dispatch subscribe]]
    [exercise-ui.client.pages :as pages]
    [exercise-ui.client.ui.welcome :refer [welcome-view]]))

(defn header-view []
  [:div.header
   [:nav
    [:a.index
     {:href (path-for :index)
      :class (when (= @pages/current-page-id
                      :index)
               "active")} "{}"]
    [:a {:href (path-for :setup)
         :class (when (= @pages/current-page-id
                         :setup)
                  "active")}
     "setup"]
    [:a {:href (path-for :exercises)
         :class (when (= @pages/current-page-id
                         :exercises)
                  "active")}
     "exercises"]
    [:a {:href (path-for :shortcuts)
         :class (when (= @pages/current-page-id
                         :shortcuts)
                  "active")}
     "VSCode keyboard shortcuts"]
    [:a {:href (path-for :reference-example)
         :class (when (= @pages/current-page-id
                         :reference-example)
                  "active")}
     "code sample"]
    [:a {:href "https://www.clojuredocs.org"
         :target "_blank"
         :rel "noopener noreferrer"}
     "clojuredocs"]
    [:a {:href "https://cognitory.github.io/clojure-cheatsheet/"
         :target "_blank"
         :rel "noopener noreferrer"}
     "clj-cheatsheet"]]

   [:div.gap]

   [:div.user
    (when @(subscribe [:logged-in?])
      [:button {:on-click (fn [_]
                            (dispatch [:log-out!]))}
       [fa/fa-sign-out-alt-solid]])]])

(defn main-view []
  [:div
   [header-view]
   [pages/current-page-view]])

(defn app-view []
  [:div
   (cond
     @(subscribe [:loading?])
     [:div.loading "Loading..."]

     :else
     [main-view])])
