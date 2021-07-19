export class Currency {
    private _id: number;
    private _shortName: string;
    private _longName: string;

    constructor(json: any) {
        this._id = json._id;
        this._shortName = json._shortName;
        this._longName = json._longName;
    }

    get id(): number {
        return this._id;
    }

    set id(value: number) {
        this._id = value;
    }

    get shortName(): string {
        return this._shortName;
    }

    set shortName(value: string) {
        this._shortName = value;
    }

    get longName(): string {
        return this._longName;
    }

    set longName(value: string) {
        this._longName = value;
    }
}
