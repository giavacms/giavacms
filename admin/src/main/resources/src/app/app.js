'use strict';

angular.module('giavacms-admin', [

    'ngMaterial'
    ,'angularMoment'
    ,'LocalStorageModule'
    ,'ui.router'
    ,'ngResource'
    ,'angular-jwt'

    ,'giavacms-misc'
    ,'giavacms-date'
    ,'giavacms-rs'
    ,'giavacms-localstorage'
    ,'giavacms-auth'
    
    ,'giavacms-config'
    ,'giavacms-menu'
    ,'giavacms-layout'
    ,'giavacms-home'

    // comment this if you don't need ACLs
//    ,'giavacms-login'

    // comment this once you have tested the layout mechianisms
    ,'giavacms-test'

    ,'giavacms-resources'
    ,'giavacms-richcontent'
   ,'giavacms-banner'
   ,'giavacms-catalogue'


]);
