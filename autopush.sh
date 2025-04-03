#!/bin/bash

#just network down

message="$1"
if [ -z "$message" ]; then
    echo "🔴 Usage: $0 <commit message> !! "
    exit 1
fi
git add .
git commit -m "${message} 🔥🙌😘🙌"
git push -u origin "$(git branch --show-current)"