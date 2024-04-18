So tips and tricks for running a 30-60 minute demo for folks at the booth. Nothing special - just based on 20 years as
an advocate / salesperson / freelancer / professional speaker:

1. As much as possible build everything around the same data set. Introducing multiple data sets burns time, drains
   enthusiasm and adds complexity. You can make an exception where the inherent nature of the data is vital to the demo
   e.g. moving from a Relational to a Graph based database. But try to minimise it as much as possible.
2. Tailor the demos you show to the folks who will be attending. Don't overload data scientists with coding terminology,
   or coders with ML nomenclature.
3. Start the demo having pre-written the 5-10 lines of code that accesses the dataset and produces some basic output.
   This means folks immediately get to see something working and don't just stand there watching you type. It also
   avoids the embarrassment of making a simple typo (due to fatigue or stress) and spending 10 mins trying to work out
   why Hello World is crashing :wink:
4. If loading data over the network check everything the night before, then check it again at the venue first thing in
   the morning. A former colleague had 100s of customers sitting watching him sweat when a networking demo failed to
   run - the reason was he had checked his email overnight and dynamic IP addressing had altered the network settings on
   his laptop.
5. Have multiple demos that build off the initial one that has been pre-written. Rehearse these as much as possible, but
   plan to vary the order based on the interests of the folks attending.
6. As much as possible try to make the demos independent. So you don't have to show both B and C to get to D.
7. Try to undo any changes at the end of each demo, so the data set is restored to the original state. Or even better,
   have a script that deletes the entire data set and rebuilds it from scratch.
8. Have prewritten saved versions of each combination of demos that you can fall back on. Ideally these should be in a
   Repo that folks can clone.
9. Have a way to run everything on your own machine in case of complete network failure.
10. Think of questions you can ask attendees as you go. If you were using a movies DB you could ask their favourite
    film. If pulling data from StackOverflow or GitHub you could ask for their ID.
11. Ideally let folks drive the demo e.g. offer five different kinds of chart to view the data and use the option that
    gets the most votes.
12. Always tell folks when the same demo will be running again that day. So they can recommend it to colleagues, and
    maybe even drag them to you personally.
