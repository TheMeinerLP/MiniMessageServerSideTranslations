# MiniMessage Server Side Translation
This project is a minimal reproducible example. That's helps me and us to reach the point of allow developers to use the kyori translation system more effective. 

## Current issue
The mini message don't get parsed and transformed to the right translatable.  

Changes from Joo2000: 
We escaped the nested lang tag and the second parameter(Playername)
Changes from LynxPlay:
Add a wrapper around your registry.

Old Output:
![img_1.png](img_1.png)
Previous Output:
![img_2.png](img_2.png)
Current Output:
![img_3.png](img_3.png)
Expected output: 
![img.png](img.png)

---
# Resolved ?
Yes, the issue is resolved but the workaround is very dirty. Maybe we can open a PR on Adventure and Paper to have a native function of that.
I think, this is now the go-to way to implement Server Side Translations. 
Please use that as an example. Also, I added a java example for the Copy and Pasta Dudes(CopyAndPasta is an AntiPattern, you know ?).

Thanks on @lynxplay, @yannicklamprecht and @joo200. Also on the discord kyori and devcord.
