export class Currency {
    public id: number;
    public shortName: string;
    public longName: string;

    constructor(json: any) {
      console.log('curr', json)
        this.id = json?.id;
        this.shortName = json?.shortName;
        this.longName = json?.longName;
    }
}
