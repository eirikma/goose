# Agent workflow

## Planning and execution
1. Understand the task and its issue. Ask when the goal or acceptance criteria are unclear.
2. Write a plan in `workflow/plans/<issue>-<short-slug>.md`: goal, acceptance criteria, and
   phases. Every phase must leave the build green (expand → migrate → contract).
3. Agree on the plan with the human before executing.
4. Execute one phase at a time. If something does not go as planned, stop and discuss rather
   than improvising a new plan.
5. Update the plan file as phases complete, and record decisions and deviations.

## Definition of done
- The build gate from the project rules is green, with no new warnings.
- New behaviour is covered by tests written first.
- Claims are backed by checkable evidence: test output, a request against the running app, or
  a screenshot. Never say a feature works or is done without it.

## No broken windows
Fix warnings, failing tests and stale documentation you touch, or raise them explicitly.
Never leave them silently.
