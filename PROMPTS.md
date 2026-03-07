# Kurly Project Prompt History

## 1. Dependency & Network Setup
- **Prompt:** 이 프로젝트에 okhttp3의 dependency를 추가하고싶어. gradle에 추가해줘
- **Prompt:** MockInterceptor에 hilt를 적용해 AssetFileProvider의 의존성을 주입하고 싶어. 어떻게 코드를 수정해야 할까?
- **Prompt:** 아주 좋아. 이제 MainRepository에서 MockInterceptor를 사용할 수 있도록 의존성을 주입해줘.
- **Prompt:** gson의 dependency를 gradle에 추가해줘
- **Prompt:** 아주 좋아. 이제 MainRepository의 getSections의 TODO를 gson을 사용해 return 값을 만들어줘.
- **Prompt:** manifest에 INTERNET permission을 추가해줘

## 2. Data Modeling & Stability
- **Prompt:** SectionsResponse에 있는 SectionInfo의 구성요소 중 type은 말 그대로 section의 type을 설정하기 위해 존재해. 이 type은 각각 "vertical", "horizontal", "grid" 라는 세 가지의 string 중 하나로 내려오는 것이 정상적인 작동이야. 코드 가독성을 위해 이 type을 String이 아닌 enum class로 만들어줘. 만약 enum class 외에 더 좋은 방법이 있다면 제시해도 좋아
- **Prompt:** 서버에서 정의되지 않은 타입이 내려올 경우도 고려해줘.
- **Prompt:** sealed class로 하는 방식과 차이점은? 만약 서버에서 잘못된 문자열이 내려왔을 때 크래시를 방지하기 좋은 방식은 둘 중에 어느 거야?
- **Prompt:** 좋아. 그럼 sealed class 방식으로 수정해줘

## 3. UI Refactoring & Paging
- **Prompt:** xml 파일들 중 item_section_*.xml 형식으로 만들어진 파일들은 전부 item_section.xml과 동일한 뷰를 가지고 있어. 이이 파일들의 동일한 뷰를 item_section.xml을 include 하는 방식으로 변경해줘
- **Prompt:** 아주 좋아. 이제 MainViewModel과 MainAdapter를 위에서 얘기했던 section type에 따라 각 xml들을 사용하도록 변경해줘. "grid"는 item_section_grid.xml을, "horizontal"은 item_section_horizontal.xml을 사용하는 형식이야. 그 외의 처리할 수 없는 경우는 item_section_default.xml을 사용하도록 만들어줘.
- **Prompt:** 아주 좋아! 이제 MainActivity에 SwipeRefreshLayout을 적용해서 pull to refresh를 해당 화면에 적용해줘.
- **Prompt:** MainAdapter의 onCreateViewHolder에서는 각 viewtype에 따라 ItemViewHolder를 만들고 있어. 각 뷰에 따라 제각기 다른 ItemViewHolder를 쓰도록 코드를 수정해줘.

## 4. Multi-ViewType Adapters & Business Logic
- **Prompt:** 아주 좋아! 이제 HorizontalViewHolder의 경우를 수정할 거야. item_section_horizontal.xml에 recyclerView를 하나 추가했어. 이 recyclerView가 horizontal하게 작동하도록 세팅하고, 이 recyclerView를 위한 adapter를 만들어줘.
- **Prompt:** 아주 좋아! 이제 MainRepository에 getSectionItems라는 api 호출 함수를 하나 추가할 거야. getSections()처럼 request를 만들고, https://kurly.com/section/products/section_products_$sectionId 라는 url을 호출하도록 해줘.
- **Prompt:** 아주 좋아! 이제 이 getSectionItems()를 통해서 위에서 만든 horizontal recyclerView에 표시할 product들을 표현하고자 해. 해당 section(horizontal)이 화면에 보여지면 getSectionItems()를 호출해서 product list를 가져오도록 코드를 추가해줘.
- **Prompt:** SectionInfo의 products는 필요없어. 이 부분을 사용하는 부분들을 모두 제거해줘.
- **Prompt:** 현재 MainAdapter의 110번째 줄 if (sectionInfo.products.isEmpty()) 에서 products가 gson parsing시에 null로 들어오면서 NPE가 발생하고 있어. products를 nullable 변수로 바꾸거나 다른 방향을 제시해줘
- **Prompt:** products가 수정되면서 MainAdapter에서 products를 사용하는 부분에서 에러가 발생하고 있어.
- **Prompt:** 이제 item_section_vertical.xml을 수정하려고 해. item_section_grid나 item_section_horizontal처럼 imageView가 있는데, vertical의 경우 가로 세로 image 비율이 6:4로 지정되었으면 좋겠어. 코드를 수정해줘
- **Prompt:** 프롬프트를 잘못 입력해서 내가 코드를 직접 수정했어. 어쨌든, 이제 ProductAdapter에 item_product_small과 item_section_vertical을 viewHolder로 구분해 함께 넣으려고 해. ProductViewHolder를 ProductSmallViewHolder로 바꾸고, item_section_vertical을 위한 viewHolder는 ProductVerticalViewHolder라는 이름으로 만들어줘.
- **Prompt:** 아주 좋아! 이제 MainAdapter의 VerticalViewHolder에서도 다른 ViewHolder처럼 productAdapter를 사용해 item인 item_product_vertical을 노출하게 해줘. 단, LinearLayoutManager를 사용하되 VERTICAL 모드로 보여지게끔 해줘. 그리고 productAdapter가 사용할 viewtype은 VIEW_TYPE_VERTICAL로 지정해줘.

## 5. Utils & Clean Architecture
- **Prompt:** item_product_vertical.xml과 item_product_small.xml에 discountPercentTextView, discountPriceTextView를 추가했어. ProductAdapter에서 화면을 보여줄 때 discountPercentTextView에는 Product 데이터에서 originalPrice와 discountedPrice의 차이를 퍼센티지로 계산해서 보여주고, discountPriceTextView에는 originalPrice를 표시해줘.
- **Prompt:** 아주 좋아. 그런데 중복되는 코드가 있는 것 같아. 중복되는 코드는 Util class로 빼줘.
- **Prompt:** Util 클래스는 Android 의존성이 없으면 좋겠어. 그러니까 지금 PriceUtil의 setPriceInfo는 TextView를 받고있어서 PriceUtil이 TextView라는 Android 의존성이 있는 상태야. 이 의존성을 제거할 수 있도록 setPriceInfo는 calculatePriceInfo와 같은 형태로 네이밍을 바꾸고, originalPrice와 discountedPrice를 받으면 그걸 계산하는 로직만 포함할 수 있도록 해줘.

## 6. Local Database (Room) & Favorite Feature
- **Prompt:** 아주 좋아! 이제 아이템 favorite 기능을 구현할 거야. item_product_small.xml과 item_product_vertical.xml의 우측 상단에 항상 보이는 image button을 하나 만들어주고, 기본 상태는 ic_btn_heart_off.xml drawable을 사용하도록 만들어줘
- **Prompt:** 아주 좋아! 이제 이 프로젝트에서 Room을 사용할 수 있도록 dependency를 추가해줘
- **Prompt:** 아주 좋아. 이제 이 앱에서 사용할 Room database table을 만들 거야. 이 table은 Product의 Int형 id값을 저장하는 테이블이야. 위에서 추가한 favorite 기능이 on 된 Product들을 저장하기 위한 테이블인 거야. 이 기능을 위한 테이블을 구성하고, 이이 테이블에 favorite 기능을 on한 id를 저장하는 쿼리, 그리고 favorite 기능을 off한 id는 제거하는 쿼리를 작성해줘
- **Prompt:** 아주 좋아! 이제 GetSectionItemsUseCase에서 mainRepository를 통해 getSectionItems()로 데이터를 가져온 후에, 이이 데이터의 Product의 id가 해당 Room 데이터베이스 테이블에 존재하는지를 체크한 후 만약 있다면 isFavorite을 true로, 아니라면 false로 두는 로직을 추가해줘
- **Prompt:** ProductAdapter에서 binding.heartButton을 클릭했을 때 Room 데이터베이스에 클릭된 product의 id를 저장하거나 delete하고싶어. room database와 쿼리는 이미 FavoriteProductDao에 정의되어 있고, 이를 활용하면 돼. heartButton이 on 될 때 id를 저장하고, heartButton을 off할 때 id를 db table에서 제거해줘

## 7. Multi-Module Refactoring
- **Prompt:** 아주 좋아! 다음은 멀티 모듈을 만들고 싶어. 현재 com.gomguk.kirly.data.mockserver 내에 있는 것들을 따로 빼서 모듈로 만들고 싶어.
