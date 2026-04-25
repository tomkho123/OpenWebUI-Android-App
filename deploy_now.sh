#!/bin/bash

echo "🚀 Deploy Open WebUI Android App"
echo "=================================="
echo ""

GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

cd /home/ubuntu/OpenWebUI-Android-App

# Check if we need to login
if ! gh auth status &> /dev/null; then
    echo -e "${YELLOW}Need to login to GitHub...${NC}"
    echo ""
    echo "Running: gh auth login"
    echo ""

    # Try non-interactive login with token
    if [ -n "$GITHUB_TOKEN" ]; then
        echo "$GITHUB_TOKEN" | gh auth login --with-token
    else
        # Interactive login
        gh auth login
    fi

    if [ $? -ne 0 ]; then
        echo -e "${RED}❌ GitHub login failed${NC}"
        echo ""
        echo "Manual steps:"
        echo "1. Tạo repo tại: https://github.com/new"
        echo "2. Chạy lệnh sau (thay YOUR_USERNAME):"
        echo "   git remote add origin https://github.com/YOUR_USERNAME/OpenWebUI-Android-App.git"
        echo "   git push -u origin main"
        exit 1
    fi
fi

echo -e "${GREEN}✓ GitHub authenticated${NC}"
gh auth status
echo ""

# Get username
USERNAME=$(gh api user --jq '.login' 2>/dev/null)
if [ -z "$USERNAME" ]; then
    USERNAME="YOUR_USERNAME"
fi
echo "Username: $USERNAME"
echo ""

# Create and push repo
echo "📦 Creating repository..."
gh repo create OpenWebUI-Android-App \
    --public \
    --description="Android app for Open WebUI with dynamic server URL input" \
    --source=. \
    --remote=origin \
    --push 2>&1

if [ $? -eq 0 ]; then
    echo ""
    echo -e "${GREEN}✅ SUCCESS!${NC}"
    echo ""
    REPO_URL="https://github.com/$USERNAME/OpenWebUI-Android-App"
    echo "📦 Repository: $REPO_URL"
    echo ""
    echo "🔨 Build APK:"
    echo "   1. Vào: $REPO_URL/actions"
    echo "   2. Click 'Build Android APK' workflow"
    echo "   3. Click 'Run workflow' → 'Run workflow'"
    echo "   4. Đợi 3-5 phút"
    echo "   5. Download APK từ Artifacts"
    echo ""

    # Open Actions page
    if command -v xdg-open &> /dev/null; then
        echo "Opening browser..."
        xdg-open "$REPO_URL/actions" 2>/dev/null &
    fi
else
    echo ""
    echo -e "${YELLOW}⚠️  Auto-create failed. Trying manual push...${NC}"
    echo ""
    
    # Try to add remote and push anyway
    git remote add origin https://github.com/$USERNAME/OpenWebUI-Android-App.git 2>/dev/null || true
    git push -u origin main 2>&1
    
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✓ Pushed successfully!${NC}"
        echo ""
        echo "Vào: https://github.com/$USERNAME/OpenWebUI-Android-App/actions"
    else
        echo -e "${RED}❌ Push failed${NC}"
        echo ""
        echo "Manual steps:"
        echo "1. Vào: https://github.com/new"
        echo "2. Tạo repo: OpenWebUI-Android-App"
        echo "3. Chạy:"
        echo "   cd /home/ubuntu/OpenWebUI-Android-App"
        echo "   git remote add origin https://github.com/YOUR_USERNAME/OpenWebUI-Android-App.git"
        echo "   git push -u origin main"
    fi
fi
