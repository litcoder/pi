# Reaction Game
A reaction game for E2E GPIO control from hardware to application level.

## Yocto build setup
* Setup dependency
```
sudo apt install libstdc++-14-dev
```


## Setup and build
* Raspberry pi - Core image base
```
# Download source
git clone git@github.com:litcoder/reaction.git
git submodule update --init --recursive

# Build yocto
export TEMPLATECONF=${PWD}/meta-reaction-game/conf/templates/mytemplate
source ./poky/oe-init-build-env build
bitbake reaction-game-image
```


## Flash image to SD card

Get the USB thumbdrive's device name from the kernel message.

```
% sudo dmesg|tail
[948404.978734] usb 1-3: New USB device strings: Mfr=1, Product=2, SerialNumber=3
[948404.978736] usb 1-3: Product: MXT USB Device
[948404.978737] usb 1-3: Manufacturer: MXTronics
[948404.978738] usb 1-3: SerialNumber: 150101v01
[948404.980033] usb-storage 1-3:1.0: USB Mass Storage device detected
[948404.980324] scsi host8: usb-storage 1-3:1.0
[948406.037615] scsi 8:0:0:0: Direct-Access     MXT-USB  Storage Device   1501 PQ: 0 ANSI: 0 CCS
[948406.037955] sd 8:0:0:0: Attached scsi generic sg2 type 0
[948406.038188] sd 8:0:0:0: [sdc] Media removed, stopped polling
[948406.038607] sd 8:0:0:0: [sdc] Attached SCSI removable disk
```

It can also be confirmed with `lsblk` command.
```
% lsblk
NAME   MAJ:MIN RM   SIZE RO TYPE MOUNTPOINTS
...
sda      8:0    0 931.5G  0 disk
├─sda1   8:1    0     1M  0 part
└─sda2   8:2    0 931.5G  0 part /
sdb      8:16   0 223.6G  0 disk
└─sdb1   8:17   0 223.6G  0 part
sdc      8:32   1     0B  0 disk
```

Flash the image into the attachted SD card and do sync explicitly to avoid copyinf failure.
```
sudo dd if=./tmp/deploy/images/raspberrypi5/reaction-game-image-raspberrypi5.rootfs.rpi-sdimg of=/dev/sdc bs=4MB status=progress conv=sync
4286578688 bytes (4.3 GB, 4.0 GiB) copied, 2 s, 1.9 GB/s
1313+0 records in
1313+0 records out
5507121152 bytes (5.5 GB, 5.1 GiB) copied, 2.64442 s, 2.1 GB/s
```

```
sudo sync
```


## Trouble shoots

### User namespaces are not usuable
```
bitbake core-image-sato
ERROR: User namespaces are not usable by BitBake, possibly due to AppArmor.
See https://discourse.ubuntu.com/t/ubuntu-24-04-lts-noble-numbat-release-notes/39890#unprivileged-user-namespace-restrictions for more information.
```

Disable sandbox
```
sudo sysctl -w kernel.unprivileged_userns_clone=0
```
