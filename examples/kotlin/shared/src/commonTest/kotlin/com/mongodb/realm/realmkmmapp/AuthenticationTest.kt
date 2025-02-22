package com.mongodb.realm.realmkmmapp

import io.realm.internal.platform.runBlocking
import io.realm.mongodb.App
import io.realm.mongodb.Credentials
import kotlin.test.Test

class AuthenticationTest: RealmTest() {

    @Test
    fun anonymousAuthTest() {
        // :code-block-start: anonymous-authentication
        val app: App = App.create(YOUR_APP_ID)
        runBlocking {
            val user = app.login(Credentials.anonymous())
        }
        // :code-block-end:
    }

    @Test
    fun emailPasswordAuthTest() {
        val email = getRandom()
        val password = getRandom()
        // :code-block-start: email-password-authentication
        val app: App = App.create(YOUR_APP_ID)
        runBlocking {
            // :hide-start:
            app.emailPasswordAuth.registerUser(email, password)
            // :hide-end:
            val user = app.login(Credentials.emailPassword(email, password))
        }
        // :code-block-end:
    }

    @Test
    fun logoutTest() {
        val app: App = App.create(YOUR_APP_ID)
        runBlocking {
            val user = app.login(Credentials.anonymous())
            // :code-block-start: log-out
            user.logOut()
            // :code-block-end:
        }
    }
}