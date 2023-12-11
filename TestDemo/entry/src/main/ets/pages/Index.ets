import { Greeting } from '../kmp/shared'

@Entry
@Component
struct Index {
  @State message: string = new Greeting().hello()

  build() {
    Row() {
      Column() {
        Text(this.message)
          .fontSize(50)
          .fontWeight(FontWeight.Bold)
      }
      .width('100%')
    }
    .height('100%')
  }
}