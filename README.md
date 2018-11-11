# java + spring-boot

Serves as an REST(hopefully) endpoints for YAAPOS. 
Which is a tool that allows you to track how much you have spent over a lifetime, as when you start using it. 
The idea behind YAAPOS is to track money spent on a daily basis, removing all other factors like when your money comes in, when you pay the rent etc etc. 
The only indicator that you should care about is $/day. 
This way, I believe, you will spend less on the day you receive your paycheck because your daily usage is all that matters.

# Installation
It can be daunting, even for me, to revisit how to set up this god forsaken program. There are several items that one have to prepare before even attempting to run this. Everything should be free to use.

1. Install Java...preferably Java8
2. Install Maven...well just extract it somewhere and add it to PATH

This should enable you to run `mvn clean install` in the directory....but running it with `java -jar target\safetaiga-1.0.jar`

You will notice however, that there are errors. Namely, you will get an issue with `NullPointerException`. This is caused by [AuthController.java](https://github.com/hbina/safetaiga/blob/master/src/main/java/com/akarin/webapp/controllers/AuthController.java). Specifically by `config.getDomain()))`, `config.getClientId()` and `config.getClientSecret()`. Since this project uses Auth0. You will have to register for an account there, create an app.....then find the values for your domain, clientId and clientSecret.

After retrieving all that, simply add it to your environment variables....I am currently writing from Windows 10 and there seems to be a problem with java not detecting environment variables....

Assuming you have done all that, you will now encounter some problems with database....well just install PostgreSQL and configure account etc etc. Finally just update your database username and password and put it as database_login.txt in project's root.