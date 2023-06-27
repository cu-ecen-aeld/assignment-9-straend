# Recipe created by recipetool
# This is the basis of a recipe and may need further editing in order to be fully functional.
# (Feel free to remove these comments when editing.)

# WARNING: the following LICENSE and LIC_FILES_CHKSUM values are best guesses - it is
# your responsibility to verify that the values are complete and correct.
#
# The following license files were not able to be identified and are
# represented as "Unknown" below, you will need to check them yourself:
#   LICENSE
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f098732a73b5f6f3430472f5b094ffdb"

SRC_URI = "git://git@github.com/cu-ecen-aeld/assignment-7-straend.git;protocol=ssh;branch=main \
           file://0001-Only-build-misc-modules-and-scull.patch \
           file://0001-PDE_DATA-replaced-by-pde_data.patch \
           file://init_scull \
           "

# Modify these as desired
PV = "1.0+git${SRCPV}"
SRCREV = "849ef219f38edc4fe5a9a39f7837ea0e2d2e0d98"
S = "${WORKDIR}/git"
inherit module

INITSCRIPT_NAME = "init_scull"
INITSCRIPT_PARAMS = "start 99 S ."
inherit update-rc.d

KERNEL_MODULE_AUTOLOAD += "scull"

# Only build the scull module
EXTRA_OEMAKE:append:task-install = " -C ${STAGING_KERNEL_DIR} M=${S}/scull"
EXTRA_OEMAKE += "KERNELDIR=${STAGING_KERNEL_DIR}"
RPROVIDES:${PN} += "kernel-module-scull"
FILES:${PN} += "${INIT_D_DIR}/${INITSCRIPT_NAME}"

do_install:append () {
    # Add startupscript
    # Remember to inherit update-rc.d
    # and include in FILES
    install -d ${D}/${INIT_D_DIR}
install -m 0755 ${WORKDIR}/init_scull ${D}/${INIT_D_DIR}/${INITSCRIPT_NAME}
}