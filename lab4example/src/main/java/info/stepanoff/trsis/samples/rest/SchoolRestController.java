/*
 * this code is available under GNU GPL v3
 * https://www.gnu.org/licenses/gpl-3.0.en.html
 */
package info.stepanoff.trsis.samples.rest;

import info.stepanoff.trsis.samples.rest.model.SchoolDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import info.stepanoff.trsis.samples.service.SchoolService;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;

import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/public/rest/schools")
@RequiredArgsConstructor
@Api(value = "SchoolsAPI", description = "API for accessing schools")
public class SchoolRestController {

    private final SchoolService schoolService;

    @RequestMapping(value = "", method = RequestMethod.GET)

    @Operation(summary = "�������� �������� �����������",
            description = "�������� �������� ����������� ������� GET",
            responses = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                        description = "�������� ����������"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401",
                        description = "��������� ��������������"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403",
                        description = "�������������� �������������, �� � ������������ ��� �������"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404",
                        description = "������ �� ������")
            })
    public ResponseEntity<List<SchoolDTO>> browse() {
        return ResponseEntity.ok(schoolService.listAll());
    }

    @RequestMapping(value = "", method = RequestMethod.POST)

    @Operation(summary = "�������� �������� �����������",
            description = "�������� �������� ����������� ������� POST",
            responses = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                        description = "�������� ����������"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401",
                        description = "��������� ��������������"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403",
                        description = "�������������� �������������, �� � ������������ ��� �������"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404",
                        description = "������ �� ������")
            })
    public ResponseEntity<List<SchoolDTO>> browsePost(Principal principal) {
        return ResponseEntity.ok(schoolService.listAll());
    }

    @Operation(summary = "�������� ����������",
            description = "��������� ����� ���� ������, ���� � ��� �� ������� ������",
            responses = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                        description = "�������� ����������"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401",
                        description = "��������� ��������������"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403",
                        description = "�������������� �������������, �� � ������������ ��� �������"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404",
                        description = "������ �� ������")
            })
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id, Principal principal) {

        if (principal == null) {
            throw new ForbiddenException();
        }

        schoolService.delete(id);
    }

    @Operation(summary = "�������� ���������� - ����� ��� ������������",
            description = "��������� ����� ���� ������, ���� � ��� �� ������� ������")
    @RequestMapping(value = "/mockdelete/{id}", method = RequestMethod.GET)
    public void mockdelete(@PathVariable("id") Integer id, Principal principal) {
        if (principal == null) {
            throw new ForbiddenException();
        }

        schoolService.delete(id);
    }

    @RequestMapping(value = "/{number}/{name}", method = RequestMethod.POST)
    @Operation(summary = "������� ����� ���������",
            description = "������� ����� ��������� - ����������� ��������",
            responses = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                        description = "�������� ����������"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401",
                        description = "��������� ��������������"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403",
                        description = "�������������� �������������, �� � ������������ ��� �������"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404",
                        description = "������ �� ������")
            })
    public ResponseEntity<SchoolDTO> add(@PathVariable("number") Integer number, @PathVariable("name") String name, Principal principal) {
        if (principal == null) {
            throw new ForbiddenException();
        }

        return ResponseEntity.ok(schoolService.add(number, name));
    }

    @Operation(summary = "����� ���������� �� ������",
            description = "����� ���������� �� ������ - ����������� ��������",
            responses = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                        description = "�������� ����������"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401",
                        description = "��������� ��������������"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403",
                        description = "�������������� �������������, �� � ������������ ��� �������"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404",
                        description = "������ �� ������")
            })
    @RequestMapping(value = "/{number}", method = RequestMethod.GET)
    public ResponseEntity<SchoolDTO> findByNumber(@PathVariable("number") Integer number) {
        return ResponseEntity.ok(schoolService.findByNumber(number));
    }

}
