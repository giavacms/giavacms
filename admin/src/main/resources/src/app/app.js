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
    ,'giavacms-resources'

    ,'giavacms-config'
    ,'giavacms-menu'
    ,'giavacms-layout'
    ,'giavacms-home'

    // comment this if you don't need ACLs and want everything to be permitted
    ,'giavacms-login'

    // comment this once you have tested the layout mechianisms
    //,'giavacms-test'

    ,'giavacms-images'
    ,'giavacms-scripts'
    ,'giavacms-styles'
    ,'giavacms-pages'
    ,'giavacms-documents'
/*
    ,'giavacms-document'
    ,'giavacms-script'
    ,'giavacms-style'
*/

    ,'giavacms-banners'
    ,'giavacms-richcontents'
    ,'giavacms-catalogue'
    ,'giavacms-scenario'


]);
