export class UserSettings {
  id: number;
  userId: number;
  firstName: string;
  lastName: string;
  middleName: string;
  about: string;
  url: string;
  phone: string;
  country: string;
  language: string;
  currency: string;
  profileImage: any;

  constructor(json: any) {
    this.id = json.id;
    this.userId = json.userId;
    this.firstName = json.firstName;
    this.lastName = json.lastName;
    this.middleName = json.middleName;
    this.about = json.about;
    this.url = json.url;
    this.phone = json.phone;
    this.country = json.country;
    this.language = json.language;
    this.currency = json.currency;
    this.profileImage=json.profileImage
    console.log(json)
  }
}
