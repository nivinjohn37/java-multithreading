#!/bin/sh

# Ask the user for a commit message
echo "Enter your commit message:"
read commitMessage

# Run the git commands
git add --all
git commit -m "$commitMessage"
git push origin main
