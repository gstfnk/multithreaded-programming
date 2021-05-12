# multithreaded-programming
IT WULS
### [Micro Project 1](micro-project-1)
Write in the appropriate programming language 4 versions of the program, calculating the definite integral of the function:
### f(x) = 3*x^3 + cos(7*x) - ln(2*x) 
on the interval [1, 40].
The method of calculation is the rectangle method, that is:
- we have some dx, e.g. 1e-4 or 1e-5,
- we calculate the sum of f (x) for x = 1, 1 + dx, 1 + 2dx, ..., c.a. twenty
- we multiply the result by dx.
Of course, we divide the domain into sub-intervals and each thread sums up in its area.
Please write and compare the operation of the following versions of the program:
##### [First method](micro-project-1/firstMethod.java)
- All threads are adding up the result in the same shared variable - no synchronization (this is, of course, the wrong version).
##### [Second method](micro-project-1/secondMethod.java)
- All threads sum the result in the same shared variable - protected by the lock.
##### [Third method](micro-project-1/thirdMethod.java)
- Each thread counts its local sum and then adds it to the global in the critical section.
##### [Fourth method](micro-project-1/fourthMethod.java)
- Some other variant. Each thread counts its local sum and, for example, we use a reduction operation - if we have such an operation in our language. At the very least, you should use e.g. tasks instead of threads. In C #, TPL is recommended, such as Parallel.For () or Parallel.Invoke ().
