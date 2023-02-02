## gitlab-wxwork-webhook-adapter

### 1. Deploy
#### java
```
mvn clean package -Dmaven.test.skip=true
java -jar target/gitlab-wxwork-webhook-adapter.jar
```

### 2. Use
1. Apply for a WeChat Work robot to get the Webhook address.
2. Record the `key` parameter.
3. Open the `Gitlab` project homepage and click `settings - integrations`
4. In the `URL` field, enter `http://machine IP:9527/webhook/${key parameter}`.
5. Check the `Trigger` you care about.
6. Click `Add webhook`
7. Enjoy it.