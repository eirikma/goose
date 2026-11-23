# Git and GitHub

## Issues
- Every task has a GitHub issue. Agents may create and comment on issues with `gh issue`.
- Reference the issue number in branch names and commits.

## Branches and commits
- Never commit to `main`. Work on a feature branch named `<issue>-<short-slug>`.
- Commit or push only when the human asks.
- Conventional commits (`feat:`, `fix:`, `refactor:`, `test:`, `docs:`, `build:`, `ci:`,
  `chore:`). Keep the first line under 50 characters. Add the issue reference in the body
  (`Refs #12`).
- Tidy First: structural and behavioural changes go in separate commits, structural first.

## Pull requests
- A person creates pull requests in the GitHub UI. Agents never run `gh pr create`, never
  merge, and never push to `main`.
- When a branch is ready, give the human a suggested PR title and description to paste.
