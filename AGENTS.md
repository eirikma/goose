# AGENTS.md

Entry point for every coding agent working in this repository.

## Read the rules first

**Read all of `.claude/rules/**/*.md` before doing anything else.** Claude Code loads them
automatically (some are scoped to file paths via `paths:` frontmatter). Other agents (Copilot CLI,
Codex, …) must read them explicitly.

- `.claude/rules/shared/` – reusable rules, not specific to this project (English): languages,
  frameworks, tools and the general agent workflow.
- `.claude/rules/project/` – rules and facts specific to Goose (Norwegian): overview, domain
  language, build and deployment.

When a shared rule and a project rule conflict, the project rule wins. Keep project-specific
names and paths out of `shared/`.

Plans go in `workflow/plans/`.

## Non-negotiables

In case you read nothing else:
- Plan first (`shared/agent-workflow.md`), agree the plan with the human, then execute phase by
  phase.
- No broken windows: `./gradlew build` green, with no warnings, before you finish.
- Every task has a GitHub issue (`shared/git-and-github.md`).
- All changes go via a feature branch and a PR made by a person in the GitHub UI. Use `gh` only
  for issues, never for PRs.
- Merging to `main` deploys to production. Never claim a feature works or is done without
  checkable evidence.
