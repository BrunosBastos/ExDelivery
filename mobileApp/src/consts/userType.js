export const UserType = {
    ADMIN: 'ADMIN',
    CLIENT: 'CLIENT',
    ANY: 'ANY',
}

export const isUserType = (isSuperUser, userType) => {
    switch (userType) {
        case UserType.ADMIN:
            return isSuperUser;
        case UserType.CLIENT:
            return !isSuperUser;
        case UserType.ANY:
            return true;
    }
}