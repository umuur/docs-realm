val config = RealmConfiguration.Builder(schema = setOf(Frog::class))
    .build()
val realm = Realm.open(config)
val tadpoles: RealmQuery<Frog> = realm.query<Frog>("age > $0", 2)
Log.v("Tadpoles: ${tadpoles.count()}")
val numTadpolesNamedJasonFunderburker = tadpoles.query("name == $0", "Jason Funderburker").count()
Log.v("Tadpoles named Jason Funderburker: $numTadpolesNamedJasonFunderburker")
