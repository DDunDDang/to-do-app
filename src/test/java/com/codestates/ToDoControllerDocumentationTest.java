package com.codestates;

import com.codestates.data.StubData;
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
//        ToDoDto.Response response = StubData.getResponse();

        given(mapper.toDoPostToToDo(Mockito.any(ToDoDto.Post.class))).willReturn(new ToDo());

        ToDo mockResultToDo = new ToDo();
        mockResultToDo.setId(1);

        given(toDoService.createToDo(Mockito.any(ToDo.class))).willReturn(mockResultToDo);
//        given(mapper.toDoToToDoResponse(Mockito.any(ToDo.class))).willReturn(response);

        // when
        ResultActions actions =
                mockMvc.perform(
                        post("/")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(StubData.getCreateContent())
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
        given(mapper.toDoPatchToToDo(Mockito.any(ToDoDto.Patch.class))).willReturn(new ToDo());

        given(toDoService.updateToDo(Mockito.any(ToDo.class))).willReturn(new ToDo());

        given(mapper.toDoToToDoResponse(Mockito.any(ToDo.class))).willReturn(StubData.getResponse());

        ResultActions actions =
                mockMvc.perform(
                patch("/{todo-id}", 1)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(StubData.getPatchContent())
                );

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
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
        given(toDoService.findToDo(Mockito.anyInt())).willReturn(new ToDo());

        given(mapper.toDoToToDoResponse(Mockito.any(ToDo.class))).willReturn(StubData.getResponse());

        mockMvc.perform(
        get("/{todo-id}", 1) // get("/" + 1L)로 진행하였으나 193번줄 코드 parameterWithName("todo-id")에서 인식하지 못함
            .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
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
        given(toDoService.findToDos()).willReturn(StubData.getToDoList());

        given(mapper.toDosToToDoResponses(Mockito.anyList())).willReturn(StubData.getResponses());

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
        doNothing().when(toDoService).deleteToDo(Mockito.anyInt());

        mockMvc.perform(
                        delete("/{todo-id}", 1)
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
