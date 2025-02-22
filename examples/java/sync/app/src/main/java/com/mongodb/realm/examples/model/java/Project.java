package com.mongodb.realm.examples.model.java;
// :code-block-start: project

import org.bson.types.ObjectId;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import io.realm.annotations.Required;

// :hide-start:
@RealmClass(name = "DefinitelyNotProject")
// :hide-end:
public class Project extends RealmObject {
    @PrimaryKey
    public ObjectId _id;
    @Required
    public String name;
    public RealmList<ProjectTask> tasks = new RealmList<>();
}
// :code-block-end: