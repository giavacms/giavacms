<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>GiavaCMS | Login</title>

</head>

<body class="gray-bg">

<div class="middle-box text-center loginscreen animated fadeInDown">
    <div>
        <div>

            <h1 class="logo-name">giavaCMS</h1>

        </div>
        <h3>Welcome to JPeppol</h3>
        <p>Login in.</p>
        <form class="m-t" role="form" method="post" action="j_security_check">
            <div class="form-group">
                <input type="text" name="j_username" class="form-control" placeholder="Username" required="">
            </div>
            <div class="form-group">
                <input type="password" name="j_password" class="form-control" placeholder="Password" required="">
            </div>
            <button type="submit" class="btn btn-primary block full-width m-b">Login</button>

        </form>
        <p class="m-t">
            <small>JPeppol &copy; 2015 - <a href="http://docs.jpeppol.com/" target="_blank" title="documentazione">
                documentazione</a></small>
        </p>
    </div>
</div>

<!-- Mainly scripts -->
<script src="/lib/jquery/jquery-2.1.1.min.js"></script>
<script src="/lib/bootstrap/bootstrap.min.js"></script>

</body>

</html>
