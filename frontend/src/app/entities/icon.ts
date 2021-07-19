export class Icon
{
    private _path: string;

    constructor(path: string) {
      this._path = path;
    }


  get path(): string {
    return this._path;
  }

  set path(value: string) {
    this._path = value;
  }
}
