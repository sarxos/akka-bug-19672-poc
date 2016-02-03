# akka-bug-19672-poc

This repo is to demonstrate Akka bug [#19672](https://github.com/akka/akka/issues/19672) reproduced.

Manual steps:

1. Start ```MainWatcher``` class,
2. Start ```MainSpawner``` class,
3. Run ```jvisualvm``` and connect to ```MainSpawner``` process,
4. Go to _Sampler_ tab and click on _Memory_ button,
5. At the bottom, in _Class name Filter_ enter ```ActorCell```,
6. Verify the number of ```ActorCell``` instances is low (you can note it),
7. Go to ```MainSpawner``` console, there should be a message _Press any key to start actors spawning_, just press the key,
8. Wait for the actors to spawn,
9. Verify the number of ```ActorCell``` instances is high - there should be 10k+ instances,
9. Go to ```MainSpawner``` console, there should be a message in console _Press any key to start actors termination_, just press it,
10. Wait for the actors to be stopped, you can switch to ```MainWatcher``` and observe when ```Terminated``` messages are not received any more,
11. In ```jvisualvm``` perform garbage collection few times (just press on _Perform GC_ button),
12. Verify the number of ```ActorCell``` instances is still high - there are 10k+ instances which are not garbage collected.
