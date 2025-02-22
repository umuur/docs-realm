package com.mongodb.realm.examples.kotlin

import android.util.Log
import com.mongodb.realm.examples.Expectation
import com.mongodb.realm.examples.RealmTest
import com.mongodb.realm.examples.getRandomPartition
import com.mongodb.realm.examples.model.kotlin.Frog
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration
import io.realm.mongodb.Credentials
import io.realm.mongodb.User
import io.realm.mongodb.sync.Subscription
import io.realm.mongodb.sync.SyncConfiguration
import java.util.concurrent.TimeUnit
import org.junit.Before
import org.junit.Test

class FlexibleSyncTest : RealmTest() {

    var YOUR_APP_ID = "android-flexible-rxwsf" // App ID for flexible sync project, since flexible sync and partition-sync cannot coexist

    @Before
    fun before() { // required because otherwise each test after the first will fail because of cached/new config conflicts.
        // we delete the "default.realm" config (if it exists) and then try to override the default realm name to something else to prevent conflicts.
        val appID = YOUR_APP_ID // replace this with your App ID
        activity!!.runOnUiThread {
            val app = App(
                AppConfiguration.Builder(appID)
                    .build()
            )
            val defConfig =
                RealmConfiguration.Builder().name("default.realm").build()
            Realm.deleteRealm(defConfig)
            // this seems to be the only way to override the default name ("default.realm") used by flexible sync. Yes, it is not a good way.
            val overrideDefaultName =
                RealmConfiguration.Builder().name(getRandomPartition()).build()
            Realm.setDefaultConfiguration(overrideDefaultName)
        }
    }

    @Test
    fun openARealm() {
        val expectation = Expectation()
        activity!!.runOnUiThread {

            // :code-block-start: open-a-realm
            // instantiate a Realm App connection
            val appID: String = YOUR_APP_ID // replace this with your App ID
            val app = App(
                AppConfiguration.Builder(appID)
                    .build()
            )
            // authenticate a user
            val credentials = Credentials.anonymous()
            app.loginAsync(
                credentials
            ) { it: App.Result<User?> ->
                if (it.isSuccess) {
                    val user = it.get()

                    // add an initial subscription to the sync configuration
                    // :code-block-start: add-a-subscription
                    val config = SyncConfiguration.Builder(app.currentUser())
                        .initialSubscriptions { realm, subscriptions ->
                            subscriptions.add(
                                Subscription.create(
                                    "subscriptionName",
                                    realm.where(Frog::class.java)
                                        .equalTo("species", "spring peeper")
                                )
                            )
                        }
                        // :hide-start:
                        .inMemory()
                        .waitForInitialRemoteData(2112, TimeUnit.MILLISECONDS)
                        // :hide-end:
                        .build()

                    // instantiate a realm instance with the flexible sync configuration
                    Realm.getInstanceAsync(config, object : Realm.Callback() {
                        override fun onSuccess(realm: Realm) {
                            Log.v("EXAMPLE", "Successfully opened a realm.")
                            // :hide-start:
                            realm.close()
                            expectation.fulfill()
                            // :hide-end:
                        }
                    })
                    // :code-block-end:
                } else {
                    Log.e(
                        "EXAMPLE",
                        "Failed to log in: " + it.error.errorMessage
                    )
                }
            }
            // :code-block-end:
        }
        expectation.await()
    }

    @Test
    fun explicitlyNamedSubscription() {
        val expectation = Expectation()
        activity!!.runOnUiThread {

            // instantiate a Realm App connection
            val appID: String = YOUR_APP_ID // replace this with your App ID
            val app = App(
                AppConfiguration.Builder(appID)
                    .build()
            )
            // authenticate a user
            val credentials = Credentials.anonymous()
            app.loginAsync(
                credentials
            ) { it: App.Result<User?> ->
                if (it.isSuccess) {
                    val user = it.get()
                    // :code-block-start: explicitly-named-subscription
                    val config = SyncConfiguration.Builder(app.currentUser())
                        .initialSubscriptions { realm, subscriptions ->
                            // add a subscription with a name
                            subscriptions.add(
                                Subscription.create(
                                    "frogSubscription",
                                    realm.where(Frog::class.java)
                                        .equalTo("species", "spring peeper")
                                )
                            )
                        }
                        // :hide-start:
                        .inMemory()
                        .waitForInitialRemoteData(2112, TimeUnit.MILLISECONDS)
                        // :hide-end:
                        .build()
                    Realm.getInstanceAsync(config, object : Realm.Callback() {
                        override fun onSuccess(realm: Realm) {
                            Log.v("EXAMPLE", "Successfully opened a realm.")
                            // later, you can look up this subscription by name
                            val subscription =
                                realm.subscriptions.find("frogSubscription")

                            // :hide-start:
                            realm.close()
                            expectation.fulfill()
                            // :hide-end:
                        }
                    })
                    // :code-block-end:
                } else {
                    Log.e(
                        "EXAMPLE",
                        "Failed to log in: " + it.error.errorMessage
                    )
                }
            }
        }
        expectation.await()
    }

    @Test
    fun implicitlyNamedSubscription() {
        val expectation = Expectation()
        activity!!.runOnUiThread {

            // instantiate a Realm App connection
            val appID: String = YOUR_APP_ID // replace this with your App ID
            val app = App(
                AppConfiguration.Builder(appID)
                    .build()
            )
            // authenticate a user
            val credentials = Credentials.anonymous()
            app.loginAsync(
                credentials
            ) { it: App.Result<User?> ->
                if (it.isSuccess) {
                    val user = it.get()
                    // :code-block-start: implicitly-named-subscription
                    val config = SyncConfiguration.Builder(app.currentUser())
                        .initialSubscriptions { realm, subscriptions ->
                            // add a subscription without assigning a name
                            subscriptions.add(
                                Subscription.create(
                                    // :hide-start:
                                    "totallyNotASubscriptionName", // conflicts between unnamed subs -- nasty hack workaround
                                    // :hide-end:
                                    realm.where(Frog::class.java)
                                        .equalTo("species", "spring peeper")
                                )
                            )
                        }
                        // :hide-start:
                        .inMemory()
                        .waitForInitialRemoteData(2112, TimeUnit.MILLISECONDS)
                        // :hide-end:
                        .build()
                    Realm.getInstanceAsync(config, object : Realm.Callback() {
                        override fun onSuccess(realm: Realm) {
                            Log.v("EXAMPLE", "Successfully opened a realm.")
                            // later, you can look up this subscription by query
                            val subscription =
                                realm.subscriptions.find(
                                    realm.where(
                                        Frog::class.java
                                    ).equalTo("species", "spring peeper")
                                )
                            // :hide-start:
                            realm.close()
                            expectation.fulfill()
                            // :hide-end:
                        }
                    })
                    // :code-block-end:
                } else {
                    Log.e(
                        "EXAMPLE",
                        "Failed to log in: " + it.error.errorMessage
                    )
                }
            }
        }
        expectation.await()
    }

    @Test
    fun waitForSubscriptionSync() {
        val expectation = Expectation()
        activity!!.runOnUiThread {

            // instantiate a Realm App connection
            val appID: String = YOUR_APP_ID // replace this with your App ID
            val app = App(
                AppConfiguration.Builder(appID)
                    .build()
            )
            // authenticate a user
            val credentials = Credentials.anonymous()
            app.loginAsync(
                credentials
            ) { it: App.Result<User?> ->
                if (it.isSuccess) {
                    val user = it.get()
                    // :code-block-start: wait-for-subscription-sync
                    val config = SyncConfiguration.Builder(app.currentUser())
                        .initialSubscriptions { realm, subscriptions ->
                            subscriptions.add(
                                Subscription.create(
                                    "my subscription",
                                    realm.where(Frog::class.java)
                                        .equalTo("species", "poison dart")
                                )
                            )
                        }
                        // :hide-start:
                        .inMemory()
                        // :hide-end:
                        .waitForInitialRemoteData(
                            2112,
                            TimeUnit.MILLISECONDS
                        )
                        .build()
                    Realm.getInstanceAsync(config, object : Realm.Callback() {
                        override fun onSuccess(realm: Realm) {
                            Log.v("EXAMPLE", "Successfully opened a realm.")
                            // :hide-start:
                            realm.close()
                            expectation.fulfill()
                            // :hide-end:
                        }
                    })
                    // :code-block-end:
                } else {
                    Log.e(
                        "EXAMPLE",
                        "Failed to log in: " + it.error.errorMessage
                    )
                }
            }
        }
        expectation.await()
    }

    @Test
    fun updateNamedSubscription() {
        val expectation = Expectation()
        activity!!.runOnUiThread {

            // instantiate a Realm App connection
            val appID: String = YOUR_APP_ID // replace this with your App ID
            val app = App(
                AppConfiguration.Builder(appID)
                    .build()
            )
            // authenticate a user
            val credentials = Credentials.anonymous()
            app.loginAsync(
                credentials
            ) { it: App.Result<User?> ->
                if (it.isSuccess) {
                    val user = it.get()
                    val config = SyncConfiguration.Builder(app.currentUser())
                        .initialSubscriptions { realm, subscriptions ->
                            subscriptions.add(
                                Subscription.create(
                                    "my frog subscription",
                                    realm.where(Frog::class.java)
                                        .equalTo("species", "treefrog")
                                )
                            )
                            subscriptions.add(
                                Subscription.create(
                                    "other frog subscription",
                                    realm.where(Frog::class.java)
                                        .equalTo("species", "cane toad")
                                )
                            )
                        }
                        // :hide-start:
                        .inMemory()
                        .waitForInitialRemoteData(2112, TimeUnit.MILLISECONDS)
                        // :hide-end:
                        .build()
                    Realm.getInstanceAsync(config, object : Realm.Callback() {
                        override fun onSuccess(realm: Realm) {
                            Log.v("EXAMPLE", "Successfully opened a realm.")
                            // :code-block-start: update-subscriptions-by-name
                            realm.subscriptions.update { subscriptions ->
                                // to update a named subscription, create a replacement with
                                // the same name and add it to the subscription set
                                subscriptions.addOrUpdate(
                                    Subscription.create(
                                        "my frog subscription",
                                        realm.where(Frog::class.java)
                                            .equalTo(
                                                "name",
                                                "Benedict Cumberburger"
                                            )
                                    )
                                )
                            }
                            // :code-block-end:
                            // :hide-start:
                            realm.close()
                            expectation.fulfill()
                            // :hide-end:
                        }
                    })
                } else {
                    Log.e(
                        "EXAMPLE",
                        "Failed to log in: " + it.error.errorMessage
                    )
                }
            }
        }
        expectation.await()
    }

    @Test
    fun updateUnnamedSubscription() {
        val expectation = Expectation()
        activity!!.runOnUiThread {

            // instantiate a Realm App connection
            val appID: String = YOUR_APP_ID // replace this with your App ID
            val app = App(
                AppConfiguration.Builder(appID)
                    .build()
            )
            // authenticate a user
            val credentials = Credentials.anonymous()
            app.loginAsync(
                credentials
            ) { it: App.Result<User?> ->
                if (it.isSuccess) {
                    val user = it.get()
                    val config = SyncConfiguration.Builder(app.currentUser())
                        .initialSubscriptions { realm, subscriptions ->
                            subscriptions.add(
                                Subscription.create(
                                    "my froggy subscription",
                                    realm.where(Frog::class.java)
                                        .equalTo("species", "treefrog")
                                )
                            )
                            subscriptions.add(
                                Subscription.create(
                                    realm.where(Frog::class.java)
                                        .equalTo("species", "cane toad")
                                )
                            )
                        }
                        // :hide-start:
                        .inMemory()
                        .waitForInitialRemoteData(2112, TimeUnit.MILLISECONDS)
                        // :hide-end:
                        .build()
                    Realm.getInstanceAsync(config, object : Realm.Callback() {
                        override fun onSuccess(realm: Realm) {
                            Log.v("EXAMPLE", "Successfully opened a realm.")
                            // :code-block-start: update-subscriptions-by-query
                            realm.subscriptions.update { subscriptions ->
                                // to update an unnamed subscription, remove it from the
                                // subscription set, then add your new query to the set
                                val mySubscription =
                                    subscriptions.find(
                                        realm.where(
                                            Frog::class.java
                                        ).equalTo(
                                            "species",
                                            "cane toad"
                                        )
                                    )
                                subscriptions.remove(mySubscription)
                                subscriptions.addOrUpdate(
                                    Subscription.create(
                                        realm.where(Frog::class.java)
                                            .equalTo(
                                                "species",
                                                "albino cane toad"
                                            )
                                    )
                                )
                            }
                            // :code-block-end:
                            // :hide-start:
                            realm.close()
                            expectation.fulfill()
                            // :hide-end:
                        }
                    })
                } else {
                    Log.e(
                        "EXAMPLE",
                        "Failed to log in: " + it.error.errorMessage
                    )
                }
            }
        }
        expectation.await()
    }

    @Test
    fun removeSingleSubscription() {
        val expectation = Expectation()
        activity!!.runOnUiThread {

            // instantiate a Realm App connection
            val appID: String = YOUR_APP_ID // replace this with your App ID
            val app = App(
                AppConfiguration.Builder(appID)
                    .build()
            )
            // authenticate a user
            val credentials = Credentials.anonymous()
            app.loginAsync(
                credentials
            ) { it: App.Result<User?> ->
                if (it.isSuccess) {
                    val user = it.get()
                    val config = SyncConfiguration.Builder(app.currentUser())
                        .initialSubscriptions { realm, subscriptions ->
                            subscriptions.add(
                                Subscription.create(
                                    "mySubscription",
                                    realm.where(Frog::class.java)
                                        .equalTo("species", "treefrog")
                                )
                            )
                            subscriptions.add(
                                Subscription.create(
                                    "myOtherSubscription",
                                    realm.where(Frog::class.java)
                                        .equalTo("species", "milky frog")
                                )
                            )
                        }
                        // :hide-start:
                        .inMemory()
                        .waitForInitialRemoteData(2112, TimeUnit.MILLISECONDS)
                        // :hide-end:
                        .build()
                    Realm.getInstanceAsync(config, object : Realm.Callback() {
                        override fun onSuccess(realm: Realm) {
                            Log.v("EXAMPLE", "Successfully opened a realm.")
                            // :code-block-start: remove-single-subscription
                            realm.subscriptions.update { subscriptions ->
                                val mySubscription =
                                    subscriptions.find("mySubscription")
                                subscriptions.remove(mySubscription)
                            }
                            // :code-block-end:
                            // :hide-start:
                            realm.close()
                            expectation.fulfill()
                            // :hide-end:
                        }
                    })
                } else {
                    Log.e(
                        "EXAMPLE",
                        "Failed to log in: " + it.error.errorMessage
                    )
                }
            }
        }
        expectation.await()
    }

    @Test
    fun removeAllSubscriptionsToAnObjectType() {
        val expectation = Expectation()
        activity!!.runOnUiThread {

            // instantiate a Realm App connection
            val appID: String = YOUR_APP_ID // replace this with your App ID
            val app = App(
                AppConfiguration.Builder(appID)
                    .build()
            )
            // authenticate a user
            val credentials = Credentials.anonymous()
            app.loginAsync(
                credentials
            ) { it: App.Result<User?> ->
                if (it.isSuccess) {
                    val user = it.get()
                    val config = SyncConfiguration.Builder(app.currentUser())
                        .initialSubscriptions { realm, subscriptions ->
                            subscriptions.add(
                                Subscription.create(
                                    "my Subscription",
                                    realm.where(Frog::class.java)
                                        .equalTo("species", "treefrog")
                                )
                            )
                        }
                        // :hide-start:
                        .inMemory()
                        .waitForInitialRemoteData(2112, TimeUnit.MILLISECONDS)
                        // :hide-end:
                        .build()
                    Realm.getInstanceAsync(config, object : Realm.Callback() {
                        override fun onSuccess(realm: Realm) {
                            Log.v("EXAMPLE", "Successfully opened a realm.")
                            // :code-block-start: remove-all-subscriptions-to-an-object-type
                            realm.subscriptions.update { subscriptions ->
                                subscriptions.removeAll(
                                    Frog::class.java
                                )
                            }
                            // :code-block-end:
                            // :hide-start:
                            realm.close()
                            expectation.fulfill()
                            // :hide-end:
                        }
                    })
                } else {
                    Log.e(
                        "EXAMPLE",
                        "Failed to log in: " + it.error.errorMessage
                    )
                }
            }
        }
        expectation.await()
    }

    @Test
    fun removeAllSubscriptions() {
        val expectation = Expectation()
        activity!!.runOnUiThread {

            // instantiate a Realm App connection
            val appID: String = YOUR_APP_ID // replace this with your App ID
            val app = App(
                AppConfiguration.Builder(appID)
                    .build()
            )
            // authenticate a user
            val credentials = Credentials.anonymous()
            app.loginAsync(
                credentials
            ) { it: App.Result<User?> ->
                if (it.isSuccess) {
                    val user = it.get()
                    val config = SyncConfiguration.Builder(app.currentUser())
                        .initialSubscriptions { realm, subscriptions ->
                            subscriptions.add(
                                Subscription.create(
                                    "treefrog sub",
                                    realm.where(Frog::class.java)
                                        .equalTo("species", "treefrog")
                                )
                            )
                        }
                        // :hide-start:
                        .inMemory()
                        .waitForInitialRemoteData(2112, TimeUnit.MILLISECONDS)
                        // :hide-end:
                        .build()
                    Realm.getInstanceAsync(config, object : Realm.Callback() {
                        override fun onSuccess(realm: Realm) {
                            Log.v("EXAMPLE", "Successfully opened a realm.")
                            // :code-block-start: remove-all-subscriptions
                            realm.subscriptions.update { subscriptions -> subscriptions.removeAll() }
                            // :code-block-end:
                            // :hide-start:
                            realm.close()
                            expectation.fulfill()
                            // :hide-end:
                        }
                    })
                } else {
                    Log.e(
                        "EXAMPLE",
                        "Failed to log in: " + it.error.errorMessage
                    )
                }
            }
        }
        expectation.await()
    }
}