package com.mongodb.realm.examples.java;

import static com.mongodb.realm.examples.RealmTestKt.YOUR_APP_ID;
import static com.mongodb.realm.examples.RealmTestKt.getRandomPartition;

import android.util.Log;

import com.mongodb.realm.examples.Expectation;
import com.mongodb.realm.examples.RealmTest;
import com.mongodb.realm.examples.model.kotlin.Frog;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.MutableSubscriptionSet;
import io.realm.mongodb.sync.Subscription;
import io.realm.mongodb.sync.SubscriptionSet;
import io.realm.mongodb.sync.SyncConfiguration;

public class FlexibleSyncTest extends RealmTest {

    String YOUR_APP_ID = "android-flexible-rxwsf"; // App ID for flexible sync project, since flexible sync and partition-sync cannot coexist

    @Before
    public void before() { // required because otherwise each test after the first will fail because of cached/new config conflicts.
        // we delete the "default.realm" config (if it exists) and then try to override the default realm name to something else to prevent conflicts.
        String appID = YOUR_APP_ID; // replace this with your App ID
        activity.runOnUiThread (() -> {
            App app = new App(new AppConfiguration.Builder(appID)
                    .build());
            RealmConfiguration defConfig = new RealmConfiguration.Builder().name("default.realm").build();
            Realm.deleteRealm(defConfig);
            // this seems to be the only way to override the default name ("default.realm") used by flexible sync. Yes, it is not a good way.
            RealmConfiguration overrideDefaultName = new RealmConfiguration.Builder().name(getRandomPartition()).build();
            Realm.setDefaultConfiguration(overrideDefaultName);
        });
    }

    @Test
    public void openARealm() {
        Expectation expectation = new Expectation();
        activity.runOnUiThread (() -> {
            // :code-block-start: open-a-realm
            // instantiate a Realm App connection
            String appID = YOUR_APP_ID; // replace this with your App ID
            App app = new App(new AppConfiguration.Builder(appID)
                    .build());
            // authenticate a user
            Credentials credentials = Credentials.anonymous();
            app.loginAsync(credentials, it -> {
                if (it.isSuccess()) {
                    User user = it.get();
                    // add an initial subscription to the sync configuration
                    // :code-block-start: add-a-subscription
                    SyncConfiguration config = new SyncConfiguration.Builder(app.currentUser())
                            .initialSubscriptions(new SyncConfiguration.InitialFlexibleSyncSubscriptions() {
                                @Override
                                public void configure(Realm realm, MutableSubscriptionSet subscriptions) {
                                    subscriptions.add(Subscription.create("subscriptionName",
                                            realm.where(Frog.class)
                                                .equalTo("species", "spring peeper")));
                                }
                            })
                            // :hide-start:
                            .inMemory()
                            .waitForInitialRemoteData(2112, TimeUnit.MILLISECONDS)
                            // :hide-end:
                            .build();

                    // instantiate a realm instance with the flexible sync configuration
                    Realm.getInstanceAsync(config, new Realm.Callback() {
                        @Override
                        public void onSuccess(Realm realm) {
                            Log.v("EXAMPLE", "Successfully opened a realm.");
                            // :hide-start:
                            realm.close();
                            expectation.fulfill();
                            // :hide-end:
                        }
                    });
                    // :code-block-end:
                } else {
                    Log.e("EXAMPLE", "Failed to log in: " + it.getError().getErrorMessage());
                }
            });
            // :code-block-end:
        });
        expectation.await();
    }

    @Test
    public void explicitlyNamedSubscription() {
        Expectation expectation = new Expectation();
        activity.runOnUiThread (() -> {
            // instantiate a Realm App connection
            String appID = YOUR_APP_ID; // replace this with your App ID
            App app = new App(new AppConfiguration.Builder(appID)
                    .build());
            // authenticate a user
            Credentials credentials = Credentials.anonymous();
            app.loginAsync(credentials, it -> {
                if (it.isSuccess()) {
                    User user = it.get();
                    // :code-block-start: explicitly-named-subscription
                    SyncConfiguration config = new SyncConfiguration.Builder(app.currentUser())
                            .initialSubscriptions(new SyncConfiguration.InitialFlexibleSyncSubscriptions() {
                                @Override
                                public void configure(Realm realm, MutableSubscriptionSet subscriptions) {
                                    // add a subscription with a name
                                    subscriptions.add(Subscription.create("frogSubscription",
                                            realm.where(Frog.class)
                                                .equalTo("species", "spring peeper")));
                                }
                            })
                            // :hide-start:
                            .inMemory()
                            .waitForInitialRemoteData(2112, TimeUnit.MILLISECONDS)
                            // :hide-end:
                            .build();

                    Realm.getInstanceAsync(config, new Realm.Callback() {
                        @Override
                        public void onSuccess(Realm realm) {
                            Log.v("EXAMPLE", "Successfully opened a realm.");
                            // later, you can look up this subscription by name
                            Subscription subscription = realm.getSubscriptions().find("frogSubscription");
                            // :hide-start:
                            realm.close();
                            expectation.fulfill();
                            // :hide-end:
                        }
                    });
                    // :code-block-end:
                } else {
                    Log.e("EXAMPLE", "Failed to log in: " + it.getError().getErrorMessage());
                }
            });
        });
        expectation.await();
    }


    @Test
    public void implicitlyNamedSubscription() {
        Expectation expectation = new Expectation();
        activity.runOnUiThread (() -> {
            // instantiate a Realm App connection
            String appID = YOUR_APP_ID; // replace this with your App ID
            App app = new App(new AppConfiguration.Builder(appID)
                    .build());
            // authenticate a user
            Credentials credentials = Credentials.anonymous();
            app.loginAsync(credentials, it -> {
                if (it.isSuccess()) {
                    User user = it.get();
                    // :code-block-start: implicitly-named-subscription
                    SyncConfiguration config = new SyncConfiguration.Builder(app.currentUser())
                            .initialSubscriptions(new SyncConfiguration.InitialFlexibleSyncSubscriptions() {
                                @Override
                                public void configure(Realm realm, MutableSubscriptionSet subscriptions) {
                                    // add a subscription without assigning a name
                                    subscriptions.add(Subscription.create(
                                            // :hide-start:
                                            "totallyNotASubscriptionName", // conflicts between unnamed subs -- nasty hack workaround
                                            // :hide-end:
                                            realm.where(Frog.class)
                                                    .equalTo("species", "spring peeper")));
                                }
                            })
                            // :hide-start:
                            .inMemory()
                            .waitForInitialRemoteData(2112, TimeUnit.MILLISECONDS)
                            // :hide-end:
                            .build();

                    Realm.getInstanceAsync(config, new Realm.Callback() {
                        @Override
                        public void onSuccess(Realm realm) {
                            Log.v("EXAMPLE", "Successfully opened a realm.");
                            // later, you can look up this subscription by query
                            Subscription subscription = realm.getSubscriptions().find(realm.where(Frog.class)
                                    .equalTo("species", "spring peeper"));
                            // :hide-start:
                            realm.close();
                            expectation.fulfill();
                            // :hide-end:
                        }
                    });
                    // :code-block-end:
                } else {
                    Log.e("EXAMPLE", "Failed to log in: " + it.getError().getErrorMessage());
                }
            });
        });
        expectation.await();
    }

    @Test
    public void waitForSubscriptionSync() {
        Expectation expectation = new Expectation();
        activity.runOnUiThread (() -> {
            // instantiate a Realm App connection
            String appID = YOUR_APP_ID; // replace this with your App ID
            App app = new App(new AppConfiguration.Builder(appID)
                    .build());
            // authenticate a user
            Credentials credentials = Credentials.anonymous();
            app.loginAsync(credentials, it -> {
                if (it.isSuccess()) {
                    User user = it.get();
                    // :code-block-start: wait-for-subscription-sync
                    SyncConfiguration config = new SyncConfiguration.Builder(app.currentUser())
                            .initialSubscriptions(new SyncConfiguration.InitialFlexibleSyncSubscriptions() {
                                @Override
                                public void configure(Realm realm, MutableSubscriptionSet subscriptions) {
                                    subscriptions.add(Subscription.create("my subscription",
                                            realm.where(Frog.class)
                                                    .equalTo("species", "poison dart")));
                                }
                            })
                            // :hide-start:
                            .inMemory()
                            // :hide-end:
                            .waitForInitialRemoteData(2112, TimeUnit.MILLISECONDS)
                            .build();

                    Realm.getInstanceAsync(config, new Realm.Callback() {
                        @Override
                        public void onSuccess(Realm realm) {
                            Log.v("EXAMPLE", "Successfully opened a realm.");
                            // :hide-start:
                            realm.close();
                            expectation.fulfill();
                            // :hide-end:
                        }
                    });
                    // :code-block-end:
                } else {
                    Log.e("EXAMPLE", "Failed to log in: " + it.getError().getErrorMessage());
                }
            });
        });
        expectation.await();
    }

    @Test
    public void updateNamedSubscription() {
        Expectation expectation = new Expectation();
        activity.runOnUiThread (() -> {
            // instantiate a Realm App connection
            String appID = YOUR_APP_ID; // replace this with your App ID
            App app = new App(new AppConfiguration.Builder(appID)
                    .build());
            // authenticate a user
            Credentials credentials = Credentials.anonymous();
            app.loginAsync(credentials, it -> {
                if (it.isSuccess()) {
                    User user = it.get();
                    SyncConfiguration config = new SyncConfiguration.Builder(app.currentUser())
                            .initialSubscriptions(new SyncConfiguration.InitialFlexibleSyncSubscriptions() {
                                @Override
                                public void configure(Realm realm, MutableSubscriptionSet subscriptions) {
                                    subscriptions.add(Subscription.create("my frog subscription",
                                            realm.where(Frog.class)
                                                    .equalTo("species", "treefrog")));
                                    subscriptions.add(Subscription.create(
                                            "other frog subscription",
                                            realm.where(Frog.class)
                                                    .equalTo("species", "cane toad")));
                                }
                            })
                            // :hide-start:
                            .inMemory()
                            .waitForInitialRemoteData(2112, TimeUnit.MILLISECONDS)
                            // :hide-end:
                            .build();

                    Realm.getInstanceAsync(config, new Realm.Callback() {
                        @Override
                        public void onSuccess(Realm realm) {
                            Log.v("EXAMPLE", "Successfully opened a realm.");

                            // :code-block-start: update-subscriptions-by-name
                            realm.getSubscriptions().update(new SubscriptionSet.UpdateCallback() {
                                @Override
                                public void update(MutableSubscriptionSet subscriptions) {
                                    // to update a named subscription, create a replacement with
                                    // the same name and add it to the subscription set
                                    subscriptions.addOrUpdate(
                                            Subscription.create("my frog subscription",
                                                    realm.where(Frog.class)
                                                        .equalTo("name",
                                                                "Benedict Cumberburger")));
                                }
                            });
                            // :code-block-end:
                            // :hide-start:
                            realm.close();
                            expectation.fulfill();
                            // :hide-end:
                        }
                    });
                } else {
                    Log.e("EXAMPLE", "Failed to log in: " + it.getError().getErrorMessage());
                }
            });
        });
        expectation.await();
    }


    @Test
    public void updateUnnamedSubscription() {
        Expectation expectation = new Expectation();
        activity.runOnUiThread (() -> {
            // instantiate a Realm App connection
            String appID = YOUR_APP_ID; // replace this with your App ID
            App app = new App(new AppConfiguration.Builder(appID)
                    .build());
            // authenticate a user
            Credentials credentials = Credentials.anonymous();
            app.loginAsync(credentials, it -> {
                if (it.isSuccess()) {
                    User user = it.get();
                    SyncConfiguration config = new SyncConfiguration.Builder(app.currentUser())
                            .initialSubscriptions(new SyncConfiguration.InitialFlexibleSyncSubscriptions() {
                                @Override
                                public void configure(Realm realm, MutableSubscriptionSet subscriptions) {
                                    subscriptions.add(Subscription.create("my froggy subscription",
                                            realm.where(Frog.class)
                                                    .equalTo("species", "treefrog")));
                                    subscriptions.add(Subscription.create(
                                            realm.where(Frog.class)
                                                    .equalTo("species", "cane toad")));
                                }
                            })
                            // :hide-start:
                            .inMemory()
                            .waitForInitialRemoteData(2112, TimeUnit.MILLISECONDS)
                            // :hide-end:
                            .build();

                    Realm.getInstanceAsync(config, new Realm.Callback() {
                        @Override
                        public void onSuccess(Realm realm) {
                            Log.v("EXAMPLE", "Successfully opened a realm.");

                            // :code-block-start: update-subscriptions-by-query
                            realm.getSubscriptions().update(new SubscriptionSet.UpdateCallback() {
                                @Override
                                public void update(MutableSubscriptionSet subscriptions) {
                                    // to update an unnamed subscription, remove it from the
                                    // subscription set, then add your new query to the set
                                    Subscription mySubscription = subscriptions.find(realm.where(Frog.class)
                                            .equalTo("species",
                                                    "cane toad"));
                                    subscriptions.remove(mySubscription);

                                    subscriptions.addOrUpdate(
                                            Subscription.create(
                                                    realm.where(Frog.class)
                                                            .equalTo("species",
                                                                    "albino cane toad")));
                                }
                            });
                            // :code-block-end:
                            // :hide-start:
                            realm.close();
                            expectation.fulfill();
                            // :hide-end:
                        }
                    });
                } else {
                    Log.e("EXAMPLE", "Failed to log in: " + it.getError().getErrorMessage());
                }
            });
        });
        expectation.await();
    }

    @Test
    public void removeSingleSubscription() {
        Expectation expectation = new Expectation();
        activity.runOnUiThread (() -> {
            // instantiate a Realm App connection
            String appID = YOUR_APP_ID; // replace this with your App ID
            App app = new App(new AppConfiguration.Builder(appID)
                    .build());
            // authenticate a user
            Credentials credentials = Credentials.anonymous();
            app.loginAsync(credentials, it -> {
                if (it.isSuccess()) {
                    User user = it.get();
                    SyncConfiguration config = new SyncConfiguration.Builder(app.currentUser())
                            .initialSubscriptions(new SyncConfiguration.InitialFlexibleSyncSubscriptions() {
                                @Override
                                public void configure(Realm realm, MutableSubscriptionSet subscriptions) {
                                    subscriptions.add(Subscription.create("mySubscription",
                                            realm.where(Frog.class)
                                                    .equalTo("species", "treefrog")));
                                    subscriptions.add(Subscription.create("myOtherSubscription",
                                            realm.where(Frog.class)
                                                    .equalTo("species", "milky frog")));
                                }
                            })
                            // :hide-start:
                            .inMemory()
                            .waitForInitialRemoteData(2112, TimeUnit.MILLISECONDS)
                            // :hide-end:
                            .build();

                    Realm.getInstanceAsync(config, new Realm.Callback() {
                        @Override
                        public void onSuccess(Realm realm) {
                            Log.v("EXAMPLE", "Successfully opened a realm.");

                            // :code-block-start: remove-single-subscription
                            realm.getSubscriptions().update(new SubscriptionSet.UpdateCallback() {
                                @Override
                                public void update(MutableSubscriptionSet subscriptions) {
                                    Subscription mySubscription = subscriptions.find("mySubscription");
                                    subscriptions.remove(mySubscription);
                                }
                            });
                            // :code-block-end:
                            // :hide-start:
                            realm.close();
                            while(!realm.isClosed()) {
                                // spin spin spin
                            }
                            expectation.fulfill();
                            // :hide-end:
                        }
                    });
                } else {
                    Log.e("EXAMPLE", "Failed to log in: " + it.getError().getErrorMessage());
                }
            });
        });
        expectation.await();
    }

    @Test
    public void removeAllSubscriptionsToAnObjectType() {
        Expectation expectation = new Expectation();
        activity.runOnUiThread (() -> {
            // instantiate a Realm App connection
            String appID = YOUR_APP_ID; // replace this with your App ID
            App app = new App(new AppConfiguration.Builder(appID)
                    .build());
            // authenticate a user
            Credentials credentials = Credentials.anonymous();
            app.loginAsync(credentials, it -> {
                if (it.isSuccess()) {
                    User user = it.get();
                    SyncConfiguration config = new SyncConfiguration.Builder(app.currentUser())
                            .initialSubscriptions(new SyncConfiguration.InitialFlexibleSyncSubscriptions() {
                                @Override
                                public void configure(Realm realm, MutableSubscriptionSet subscriptions) {
                                    subscriptions.add(Subscription.create("my Subscription",
                                            realm.where(Frog.class)
                                                    .equalTo("species", "treefrog")));
                                }
                            })
                            // :hide-start:
                            .inMemory()
                            .waitForInitialRemoteData(2112, TimeUnit.MILLISECONDS)
                            // :hide-end:
                            .build();

                    Realm.getInstanceAsync(config, new Realm.Callback() {
                        @Override
                        public void onSuccess(Realm realm) {
                            Log.v("EXAMPLE", "Successfully opened a realm.");

                            // :code-block-start: remove-all-subscriptions-to-an-object-type
                            realm.getSubscriptions().update(new SubscriptionSet.UpdateCallback() {
                                @Override
                                public void update(MutableSubscriptionSet subscriptions) {
                                    subscriptions.removeAll(Frog.class);
                                }
                            });
                            // :code-block-end:
                            // :hide-start:
                            realm.close();
                            expectation.fulfill();
                            // :hide-end:
                        }
                    });
                } else {
                    Log.e("EXAMPLE", "Failed to log in: " + it.getError().getErrorMessage());
                }
            });
        });
        expectation.await();
    }

    @Test
    public void removeAllSubscriptions() {
        Expectation expectation = new Expectation();
        activity.runOnUiThread (() -> {
            // instantiate a Realm App connection
            String appID = YOUR_APP_ID; // replace this with your App ID
            App app = new App(new AppConfiguration.Builder(appID)
                    .build());
            // authenticate a user
            Credentials credentials = Credentials.anonymous();
            app.loginAsync(credentials, it -> {
                if (it.isSuccess()) {
                    User user = it.get();
                    SyncConfiguration config = new SyncConfiguration.Builder(app.currentUser())
                            .initialSubscriptions(new SyncConfiguration.InitialFlexibleSyncSubscriptions() {
                                @Override
                                public void configure(Realm realm, MutableSubscriptionSet subscriptions) {
                                    subscriptions.add(Subscription.create("treefrog sub",
                                            realm.where(Frog.class)
                                                    .equalTo("species", "treefrog")));
                                }
                            })
                            // :hide-start:
                            .inMemory()
                            .waitForInitialRemoteData(2112, TimeUnit.MILLISECONDS)
                            // :hide-end:
                            .build();

                    Realm.getInstanceAsync(config, new Realm.Callback() {
                        @Override
                        public void onSuccess(Realm realm) {
                            Log.v("EXAMPLE", "Successfully opened a realm.");

                            // :code-block-start: remove-all-subscriptions
                            realm.getSubscriptions().update(new SubscriptionSet.UpdateCallback() {
                                @Override
                                public void update(MutableSubscriptionSet subscriptions) {
                                    subscriptions.removeAll();
                                }
                            });
                            // :code-block-end:
                            // :hide-start:
                            realm.close();
                            expectation.fulfill();
                            // :hide-end:
                        }
                    });
                } else {
                    Log.e("EXAMPLE", "Failed to log in: " + it.getError().getErrorMessage());
                }
            });
        });
        expectation.await();
    }
}
