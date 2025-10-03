# Database Setup Guide

## Prerequisites

- MySQL Server 8.0 or higher installed
- MySQL running on localhost:3306

## Installation Steps

### 1. Install MySQL (if not installed)

**Windows:**
- Download from https://dev.mysql.com/downloads/installer/
- Run installer and follow wizard
- Set root password

**macOS:**
```bash
brew install mysql
brew services start mysql
mysql_secure_installation