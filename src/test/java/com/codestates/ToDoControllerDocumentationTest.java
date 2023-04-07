package com.codestates;

import com.codestates.todo.dto.ToDoDto;
import com.codestates.todo.entity.ToDo;
import com.codestates.todo.mapper.ToDoMapper;
import com.codestates.todo.controller.ToDoController;
import com.codestates.todo.service.ToDoService;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static com.codestates.util.ApiDocumentUtils.getRequestPreProcessor;
import static com.codestates.util.ApiDocumentUtils.getResponsePreProcessor;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ToDoController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureRestDocs
@MockBean(JpaMetamodelMappingContext.class)
public class ToDoControllerDocumentationTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ToDoService toDoService;
    @MockBean
    private ToDoMapper mapper;

    @Test
    @DisplayName("ToDo App 목록 생성 테스트")
    public void createToDoTest() throws Exception {
        //given
        String content = "{\n" +
                "\"title\": \"테스트하기\",\n" +
                "\"todo_order\": 1,\n" +
                "\"completed\": true\n" +
                "}";

//        ToDoDto.Response response = ToDoDto.Response.builder()
//                                .id(1L)
//                                .title("테스트하기")
//                                .todo_order(1)
//                                .completed(true)
//                                .build();

        given(mapper.toDoPostToToDo(Mockito.any(ToDoDto.Post.class))).willReturn(new ToDo());

        ToDo mockResultToDo = new ToDo();
        mockResultToDo.setId(1L);

        given(toDoService.createToDo(Mockito.any(ToDo.class))).willReturn(mockResultToDo);
//        given(mapper.toDoToToDoResponse(Mockito.any(ToDo.class))).willReturn(response);

        // when
        ResultActions actions =
                mockMvc.perform(
                        post("/")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );

        actions
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", is(startsWith("/"))))
//                .andExpect(jsonPath("$.id").value(1L))
//                .andExpect(jsonPath("$.title").value("테스트하기"))
//                .andExpect(jsonPath("$.todo_order").value(1))
//                .andExpect(jsonPath("$.completed").value(true))
                .andDo(print())
                .andDo(document("post-todo",getRequestPreProcessor(), getResponsePreProcessor(),requestFields(
                                List.of(
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("할 일"),
                                        fieldWithPath("todo_order").type(JsonFieldType.NUMBER).description("우선 순위"),
                                        fieldWithPath("completed").type(JsonFieldType.BOOLEAN).description("시행 여부"))),
                        responseHeaders(headerWithName(HttpHeaders.LOCATION).description("Location header. 등록된 리소스의 URI"))
                ));

    }

    @Test
    @DisplayName("ToDo App 목록 갱신 테스트")
    public void patchToDoTest() throws Exception {
        // given
        String content = "{\n" +
                "    \"id\" : \"1\",\n" +
                "    \"title\" : \"테스트하기\",\n" +
                "    \"todo_order\" : 1,\n" +
                "    \"completed\" : true\n" +
                "}";

        ToDoDto.Response response = ToDoDto.Response.builder()
                .id(1L)
                .title("테스트하기")
                .todo_order(1)
                .completed(true)
                .build();

        given(mapper.toDoPatchToToDo(Mockito.any(ToDoDto.Patch.class))).willReturn(new ToDo());

        given(toDoService.updateToDo(Mockito.any(ToDo.class))).willReturn(new ToDo());

        given(mapper.toDoToToDoResponse(Mockito.any(ToDo.class))).willReturn(response);

        ResultActions actions =
                mockMvc.perform(
                patch("/{todo-id}", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                );

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("테스트하기"))
                .andExpect(jsonPath("$.todo_order").value(1))
                .andExpect(jsonPath("$.completed").value(true))
                .andDo(print())
                .andDo(document("patch-todo", getRequestPreProcessor(), getResponsePreProcessor(), pathParameters(parameterWithName("todo-id").description("To-Do 식별자")),
                        requestFields(
                                List.of(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("To-Do 식별자").ignored(),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("할 일").optional(),
                                        fieldWithPath("todo_order").type(JsonFieldType.NUMBER).description("우선 순위").optional(),
                                        fieldWithPath("completed").type(JsonFieldType.BOOLEAN).description("시행 여부").optional()
                                )
                        ), responseFields(
                                List.of(
//                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("To-Do 식별자"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("할 일"),
                                        fieldWithPath("todo_order").type(JsonFieldType.NUMBER).description("우선 순위"),
                                        fieldWithPath("completed").type(JsonFieldType.BOOLEAN).description("시행 여부")
                                )
                        )));
    }

    @Test
    @DisplayName("ToDo App 목록 개별 조회 테스트")
    public void getToDoTest() throws Exception {

        ToDoDto.Response response = ToDoDto.Response.builder()
                .id(1L)
                .title("테스트하기")
                .todo_order(1)
                .completed(true)
                .build();

        given(toDoService.findToDo(Mockito.anyLong())).willReturn(new ToDo());

        given(mapper.toDoToToDoResponse(Mockito.any(ToDo.class))).willReturn(response);

        mockMvc.perform(
        get("/{todo-id}", 1L) // get("/" + 1L)로 진행하였으나 193번줄 코드 parameterWithName("todo-id")에서 인식하지 못함
            .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.title").value("테스트하기"))
            .andExpect(jsonPath("$.todo_order").value(1))
            .andExpect(jsonPath("$.completed").value(true))
            .andDo(print())
            .andDo(document("get-todo", getRequestPreProcessor(), getResponsePreProcessor(), pathParameters(parameterWithName("todo-id").description("To-Do 식별자")),
                    responseFields(
                            List.of(
                                    fieldWithPath("id").type(JsonFieldType.NUMBER).description("To-Do 식별자"),
                                    fieldWithPath("title").type(JsonFieldType.STRING).description("할 일"),
                                    fieldWithPath("todo_order").type(JsonFieldType.NUMBER).description("우선 순위"),
                                    fieldWithPath("completed").type(JsonFieldType.BOOLEAN).description("시행 여부")
                            )
                    )
            ));
    }

    @Test
    @DisplayName("ToDo App 목록 전체 조회 테스트")
    public void getToDosTest() throws Exception {
        // given
        ToDo toDo1 = new ToDo();
        toDo1.setId(1L);
        toDo1.setTitle("테스트하기");
        toDo1.setToDoOrder(1);
        toDo1.setCompleted(true);

        ToDo toDo2 = new ToDo();
        toDo2.setId(2L);
        toDo2.setTitle("잠자기");
        toDo2.setToDoOrder(2);
        toDo2.setCompleted(false);


        List<ToDoDto.Response> responses = List.of(
                ToDoDto.Response.builder()
                        .id(1L)
                        .title("테스트하기")
                        .todo_order(1)
                        .completed(true)
                        .build(),
                ToDoDto.Response.builder()
                        .id(2L)
                        .title("잠자기")
                        .todo_order(2)
                        .completed(false)
                        .build()
        );

        given(toDoService.findToDos()).willReturn(List.of(toDo1, toDo2));

        given(mapper.toDosToToDoResponses(Mockito.anyList())).willReturn(responses);

        ResultActions actions =
                mockMvc.perform(
                get("/")
                        .accept(MediaType.APPLICATION_JSON)
                );

        MvcResult result = actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andDo(print())
                .andDo(document("get-todos", getRequestPreProcessor(), getResponsePreProcessor(),
                        responseFields(
                                List.of(
                                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("To-Do 식별자"),
                                        fieldWithPath("[].title").type(JsonFieldType.STRING).description("할 일"),
                                        fieldWithPath("[].todo_order").type(JsonFieldType.NUMBER).description("우선 순위"),
                                        fieldWithPath("[].completed").type(JsonFieldType.BOOLEAN).description("시행 여부")
                                )
                        )
                ))
                .andReturn();

        List list = JsonPath.parse(result.getResponse().getContentAsString()).read("$");

        assertThat(list.size(), is(2));
    }

    @Test
    @DisplayName("ToDo App 목록 개별 삭제 테스트")
    public void deleteToDoTest() throws Exception {
        doNothing().when(toDoService).deleteToDo(Mockito.anyLong());

        mockMvc.perform(
                        delete("/{todo-id}", 1L)
                )
                .andExpect(status().isNoContent())
                .andDo(document("delete-todo", getRequestPreProcessor(), getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("todo-id").description("To-Do 식별자")
                        )));

    }
    @Test
    @DisplayName("ToDo App 목록 전체 삭제 테스트")
    public void deleteToDosTest() throws Exception {
        doNothing().when(toDoService).deleteToDos();

        mockMvc.perform(
                        delete("/")
                )
                .andExpect(status().isNoContent())
                .andDo(document("delete-todos", getRequestPreProcessor(), getResponsePreProcessor()));
    }
}
