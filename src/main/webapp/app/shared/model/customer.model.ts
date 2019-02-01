export interface ICustomer {
    id?: number;
    firstName?: string;
    lastName?: string;
    areaCode?: number;
    address?: string;
    phone?: string;
}

export class Customer implements ICustomer {
    constructor(
        public id?: number,
        public firstName?: string,
        public lastName?: string,
        public areaCode?: number,
        public address?: string,
        public phone?: string
    ) {}
}
