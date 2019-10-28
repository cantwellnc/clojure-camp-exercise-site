(ns exercise-ui.client.ui.app
  (:require
    [clojure.string :as string]
    [bloom.commons.pages :refer [path-for]]
    [bloom.commons.fontawesome :as fa]
    [re-frame.core :refer [dispatch subscribe]]
    [reagent.core :as r]
    [exercise-ui.client.pages :as pages]
    [exercise-ui.utils :as utils]))

(defn header-view []
  [:div.header
   [:h1 "{}"]
   [:nav
    [:a.main {:href (path-for :exercises)
              :class (when (= @pages/current-page-id
                              :exercises)
                       "active")}
     "exercises"]
    [:a {:href (path-for :setup)
         :class (when (= @pages/current-page-id
                         :setup)
                  "active")}
     "setup"]
    [:a {:href (path-for :shortcuts)
         :class (when (= @pages/current-page-id
                         :shortcuts)
                  "active")}
     "shortcuts"]
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
     "clj-cheatsheet"]
    [:a {:href (path-for :pastebin)
         :class (when (= @pages/current-page-id
                         :pastebin)
                  "active")}
     "share"]]

   [:div.gap]

   [:div.user
    @(subscribe [:user-id])
    [:button {:on-click (fn [e]
                          (dispatch [:log-out!]))}
     [fa/fa-sign-out-alt-solid]]]])

(defn main-view []
  [:div
   [header-view]
   [pages/current-page-view]])

(defn login-view []
  (let [user-id (r/atom "")]
    (fn []
      [:div.log-in
       [:h1 "Bell Media Clojure Training"]
       [:form
        {:on-submit
         (fn [e]
           (.preventDefault e)
           (when (not (string/blank? @user-id))
             (dispatch [:log-in! @user-id])))}
        [:input {:type "text"
                 :on-change (fn [e]
                              (reset! user-id (-> (.. e -target -value)
                                                  (utils/sanitize-user-id))))
                 :value @user-id}]
        [:button "Log In"]]])))

(defn app-view []
  [:div
   (cond
     @(subscribe [:loading?])
     [:div.loading "Loading..."]
     @(subscribe [:logged-in?])
     [main-view]
     :else
     [login-view])])
