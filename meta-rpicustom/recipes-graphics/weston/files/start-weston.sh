#!/bin/sh

# Script to manually start Weston with proper environment
# This is useful for debugging or manual testing

# Set up the runtime directory
export XDG_RUNTIME_DIR="/run/user/$(id -u)"
export XDG_SESSION_TYPE=wayland
export XDG_SESSION_CLASS=user
export XDG_SEAT=seat0
export XDG_VTNR=1

# Additional environment variables for stability
export WAYLAND_DEBUG=0
export WESTON_LOG_VERBOSITY=1

# Create runtime directory if it doesn't exist
mkdir -p "$XDG_RUNTIME_DIR"
chmod 0700 "$XDG_RUNTIME_DIR"

# Check if we're running as root and adjust accordingly
if [ "$EUID" -eq 0 ]; then
    export XDG_RUNTIME_DIR="/run/user/0"
    mkdir -p "$XDG_RUNTIME_DIR"
    chmod 0700 "$XDG_RUNTIME_DIR"
fi

echo "Starting Weston with XDG_RUNTIME_DIR=$XDG_RUNTIME_DIR"

# Start Weston with stability options
exec /usr/bin/weston --log=/var/log/weston.log --idle-time=0 "$@"
