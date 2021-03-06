<!DOCTYPE html>
<html ng-app="giavacms-admin">
<head>
    <title>GiavaCms Administration Panel</title>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="description" content="">
    <meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no"/>

    <link rel="icon" type="image/png" href="images/favicon.png"/>


    <link rel="stylesheet" href="css/fonts.gooleapis.com.css"/>
    <link rel="stylesheet" href="css/custom.css"/>
    <link rel="stylesheet" href="css/angular-chart.css"/>

    <link rel="stylesheet" href="css/angular-material.css"/>
    <link rel="stylesheet" href="css/fullcalendar.css"/>
    <link rel="stylesheet" href="css/font-awesome.css"/>
    <link rel="stylesheet" href="css/textAngular.css"/>
    <link rel="stylesheet" href="css/weather-icons.css"/>

</head>
<body ng-class="bodyClasses">
<!--[if lt IE 10]>
<p class="browsehappy">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade
    your browser</a> to improve your experience.</p>
<![endif]-->

<div class="full-height" ui-view>

</div>

<!-- MI MANCA QUESTA -->



<script src="libs/jquery.js"></script>
<script src="libs/angular.js"></script>
<script src="libs/angular-animate.js"></script>
<script src="libs/Chart.js"></script>
<script src="libs/angular-chart.js"></script>
<script src="libs/angular-cookies.js"></script>
<script src="libs/angular-jwt.js"></script>
<%--<script src="libs/angular-local-storage.js"></script>--%>
<script src="libs/angular-aria.js"></script>
<script src="libs/angular-material.js"></script>
<script src="libs/angular-messages.js"></script>
<script src="libs/moment.js"></script>
<script src="libs/angular-moment.js"></script>
<script src="libs/angular-resource.js"></script>
<script src="libs/angular-sanitize.js"></script>
<script src="libs/ui-ace.js"></script>
<script src="libs/angular-ui-tinymce.js"></script>
<script src="libs/tinymce.js"></script>

<script type="text/javascript" src="libs/ace.js"></script>
<script type="text/javascript" src="libs/mode-html.js"></script>
<script type="text/javascript" src="libs/mode-javascript.js"></script>
<script type="text/javascript" src="libs/theme-github.js"></script>




<script src="libs/fullcalendar.js"></script>
<script src="libs/calendar.js"></script>
<script src="libs/angular-ui-router.js"></script>

<script src="libs/rangy-core.js"></script>
<script src="libs/rangy-classapplier.js"></script>
<script src="libs/rangy-highlighter.js"></script>
<script src="libs/rangy-selectionsaverestore.js"></script>
<script src="libs/rangy-serializer.js"></script>
<script src="libs/rangy-textrange.js"></script>


<script src="libs/textAngular.js"></script>
<script src="libs/textAngular-sanitize.js"></script>
<script src="libs/textAngularSetup.js"></script>


<script src="libs/lang-all.js"></script>


<script src="components/util/misc/misc.module.js"></script>
<script src="components/util/misc/utils.service.js"></script>
<script src="components/util/misc/distinct.filter.js"></script>
<script src="components/util/date/date.module.js"></script>
<script src="components/util/date/mydatepicker.directive.js"></script>
<script src="components/table/table.module.js"></script>
<script src="components/table/tsmallpager-directive.js"></script>
<script src="components/table/tpager-directive.js"></script>
<script src="components/table/theader-directive.js"></script>
<script src="components/table/tfooter-directive.js"></script>
<%--<script src="components/storage/localstorage.module.js"></script>--%>
<%--<script src="components/storage/storage.service.js"></script>--%>
<script src="components/rest/rs.module.js"></script>
<script src="components/rest/rs-view-controller.function.js"></script>
<script src="components/rest/rs-service.function.js"></script>
<script src="components/rest/rs-resource.factory.js"></script>
<script src="components/rest/rs-list-controller.function.js"></script>
<script src="components/rest/rs-edit-controller.function.js"></script>
<script src="components/resources/resources.module.js"></script>
<script src="components/resources/resource-controller.function.js"></script>
<script src="components/resources/fileread.directive.js"></script>
<script src="components/resources/addfiles-controller.function.js"></script>
<script src="components/auth/auth.module.js"></script>
<%--<script src="components/auth/token.interceptor.js"></script>--%>
<script src="components/auth/permit.directive.js"></script>
<script src="components/auth/authentication.service.js"></script>
<script src="app/security.module.js"></script>
<script src="app/users/users.service.js"></script>
<script src="app/users/users-list.controller.js"></script>
<script src="app/users/users-edit.controller.js"></script>
<script src="app/test.module.js"></script>
<script src="app/test/test.controller.js"></script>
<script src="app/styles.module.js"></script>
<script src="app/styles/styles.controller.js"></script>
<script src="app/scripts.module.js"></script>
<script src="app/scripts/scripts.controller.js"></script>
<script src="app/scenario.module.js"></script>
<script src="app/scenarios/scenarios.service.js"></script>
<script src="app/scenarios/scenarios-products.service.js"></script>
<script src="app/scenarios/scenarios-list.controller.js"></script>
<script src="app/scenarios/scenarios-edit.controller.js"></script>
<script src="app/scenarios/scenarios-edit-products.controller.js"></script>
<script src="app/scenarios/scenarios-edit-images.controller.js"></script>
<script src="app/scenarios/scenarios-edit-documents.controller.js"></script>
<script src="app/scenarios/scenarios-categories.service.js"></script>
<script src="app/scenarioconfiguration/scenarioconfiguration.service.js"></script>
<script src="app/scenarioconfiguration/scenarioconfiguration.controller.js"></script>
<script src="app/roles/roles.service.js"></script>
<script src="app/roles/roles-list.controller.js"></script>
<script src="app/richcontents.module.js"></script>
<script src="app/richcontenttypes/richcontenttypes.service.js"></script>
<script src="app/richcontenttypes/richcontenttypes-new.controller.js"></script>
<script src="app/richcontenttypes/richcontenttypes-list.controller.js"></script>
<script src="app/richcontents/richcontents.service.js"></script>
<script src="app/richcontents/richcontents-list.controller.js"></script>
<script src="app/richcontents/richcontents-edit.controller.js"></script>
<script src="app/richcontents/richcontents-edit-images.controller.js"></script>
<script src="app/richcontents/richcontents-edit-documents.controller.js"></script>
<script src="app/catalogue.module.js"></script>
<script src="app/products/products.service.js"></script>
<script src="app/products/products-list.controller.js"></script>
<script src="app/products/products-edit.controller.js"></script>
<script src="app/products/products-edit-images.controller.js"></script>
<script src="app/products/products-edit-documents.controller.js"></script>
<script src="app/pages.module.js"></script>
<script src="app/pages/pages.controller.js"></script>
<script src="app/menu.module.js"></script>
<script src="app/menu/menutoggle.directive.js"></script>
<script src="app/menu/menulink.directive.js"></script>
<script src="app/menu/menuheading.directive.js"></script>
<script src="app/menu/menu.service.js"></script>
<%--<script src="app/login.module.js"></script>--%>
<%--<script src="app/login/login.controller.js"></script>--%>
<script src="app/layout.module.js"></script>
<script src="app/layout/layout.controller.js"></script>
<script src="app/images.module.js"></script>
<script src="app/images/images.controller.js"></script>
<script src="app/home.module.js"></script>
<script src="app/home/home.controller.js"></script>
<script src="app/features/features.service.js"></script>
<script src="app/features/features-new.controller.js"></script>
<script src="app/features/features-list.controller.js"></script>
<script src="app/emailconfiguration/emailconfiguration.service.js"></script>
<script src="app/emailconfiguration/emailconfiguration.controller.js"></script>
<script src="app/documents.module.js"></script>
<script src="app/documents/documents.controller.js"></script>
<script src="app/categories/categories.service.js"></script>
<script src="app/categories/categories-list.controller.js"></script>
<script src="app/categories/categories-edit.controller.js"></script>
<script src="app/catalogueconfiguration/catalogueconfiguration.service.js"></script>
<script src="app/catalogueconfiguration/catalogueconfiguration.controller.js"></script>
<script src="app/banners.module.js"></script>
<script src="app/bannertypes/bannertypes.service.js"></script>
<script src="app/bannertypes/bannertypes-new.controller.js"></script>
<script src="app/bannertypes/bannertypes-list.controller.js"></script>
<script src="app/banners/banners.service.js"></script>
<script src="app/banners/banners-list.controller.js"></script>
<script src="app/banners/banners-edit.controller.js"></script>
<script src="app/config.module.js"></script>
<script src="app/app.js"></script>


</body>
</html>

