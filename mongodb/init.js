db = db.getSiblingDB("mongodb");
db.getCollection("__bootstrap").insertOne({ createdAt: new Date() });
db.getCollection("__bootstrap").deleteMany({});
