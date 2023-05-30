#!/bin/bash

echo "#!/bin/sh" >> ./githooks/prepare-commit-msg
echo "./gradlew addKtlintCheckGitPreCommitHook" >> ./githooks/prepare-commit-msg

echo "COMMIT_MSG_FILE=\$1" >> ./githooks/prepare-commit-msg
echo "COMMIT_SOURCE=\$2" >> ./githooks/prepare-commit-msg
echo "SHA1=\$3" >> ./githooks/prepare-commit-msg

echo "/usr/bin/perl -i.bak -ne 'print unless(m/^. Please enter the commit message/..m/^#$/)' \"\$COMMIT_MSG_FILE\"" >> ./githooks/prepare-commit-msg