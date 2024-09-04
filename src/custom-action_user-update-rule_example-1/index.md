# Custom Action 

## Overview

In this forum post, the JAR provided works as an External JAR Job, but not a Custom Action User Update Rule.

https://forums.saviynt.com/t5/identity-governance/custom-action-external-jar-in-user-update-rule/m-p/29164

To use with as a Custom Action, the entry method should be like:

```
public void customMethod(String userJson)
```

For example:

```
public void customMethod(String userJson) {
   System.out.println("userJson data : " + userJson);
}
```

## Code

Click here to [view the code on GitHub]().

## Dependencies

https://mvnrepository.com/artifact/com.google.code.gson/gson/2.8.1

